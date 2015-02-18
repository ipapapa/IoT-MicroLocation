//
//  CDSBeaconLandmark.m
//  micro-location
//
//  Created by Fahim Zafari on 2/18/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import "CDSBeaconLandmark.h"
#import "UIColor+Interpolation.h"
@interface CDSBeaconLandmark ()

// Set-once stuff
@property (nonatomic, strong) NSString* ident;
@property (nonatomic, assign) double x;
@property (nonatomic, assign) double y;
@property (nonatomic, strong) UIColor* color;

// Calculated from rssi
@property (nonatomic, assign) double meanRssi;
@property (nonatomic, assign) double stdDeviationRssi;
@property (nonatomic, assign) double meters;
@property (nonatomic, assign) double meanMeters;
@property (nonatomic, assign) double meanMetersVariance;

// Ring-buffer for running average, Std Dev.
#define RSSIBUFFERSIZE 30
@property (nonatomic, assign) NSInteger* rssiBuffer;
@property (nonatomic, assign) NSInteger bufferIndex;
@property (nonatomic, assign) BOOL bufferFull;

@end


#define INTERFERENCEFUDGEFACTOR 1.75


@implementation CDSBeaconLandmark

+ (CDSBeaconLandmark*)landmarkWithIdent:(NSString*)ident x:(double)x y:(double)y color:(UIColor*)color {
    CDSBeaconLandmark* ret = [[CDSBeaconLandmark alloc] init];
    ret.ident = ident;
    ret.x = x;
    ret.y = y;
    ret.color = color;
    
    ret.rssiBuffer = malloc(RSSIBUFFERSIZE*sizeof(NSInteger));
    
    return ret;
}

- (void)setRssi:(NSInteger)rssi {
    _rssi = rssi;
    
    // Ignore zeros in average, StdDev -- we clear the value before setting it to
    // prevent old values from hanging around if there's no reading
    if (rssi == 0) {
        self.meters = 0;
        return;
    }
    
    self.meters = [self metersFromRssi:rssi];
    
    NSInteger* pidx = self.rssiBuffer;
    *(pidx+self.bufferIndex++) = rssi;
    
    if (self.bufferIndex >= RSSIBUFFERSIZE) {
        self.bufferIndex %= RSSIBUFFERSIZE;
        self.bufferFull = YES;
    }
    
    if (self.bufferFull) {
        
        // Only calculate trailing mean and Std Dev when we have enough data
        double accumulator = 0;
        for (NSInteger i = 0; i < RSSIBUFFERSIZE; i++) {
            accumulator += *(pidx+i);
        }
        self.meanRssi = accumulator / RSSIBUFFERSIZE;
        self.meanMeters = [self metersFromRssi:self.meanRssi];
        
        accumulator = 0;
        for (NSInteger i = 0; i < RSSIBUFFERSIZE; i++) {
            NSInteger difference = *(pidx+i) - self.meanRssi;
            accumulator += difference*difference;
        }
        self.stdDeviationRssi = sqrt( accumulator / RSSIBUFFERSIZE);
        self.meanMetersVariance = ABS(
                                      [self metersFromRssi:self.meanRssi]
                                      - [self metersFromRssi:self.meanRssi+self.stdDeviationRssi]
                                      );
    }
    
}


#pragma mark Helpers

+ (NSString*)identFromMajor:(NSInteger)major minor:(NSInteger)minor {
    return [NSString stringWithFormat:@"%ld/%ld", (long)major, (long)minor];
}

- (double)metersFromRssi:(NSInteger)rssi {
    
    // Based on measurement of Estimote beacons in open air, power = 0
    // double ret = 0.0007109 * expf(0.1114483*-rssi);
    
    // Based on measurement of Estimote beacons in open air, power = -8
    double ret = 0.0003351 * exp(0.1103220*-rssi);
    
    ret *= INTERFERENCEFUDGEFACTOR;
    
    return ret;
}
@end




