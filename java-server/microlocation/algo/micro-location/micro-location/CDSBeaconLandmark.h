//
//  CDSBeaconLandmark.h
//  micro-location
//
//  Created by Fahim Zafari on 2/18/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UIColor+Interpolation.h"
@interface CDSBeaconLandmark : NSObject

@property (nonatomic, readonly) NSString* ident;
@property (nonatomic, readonly) double x;
@property (nonatomic, readonly) double y;
@property (nonatomic, readonly) UIColor*  color;

@property (nonatomic, assign) NSInteger rssi;
// Trailing average and standard deviation of RSSI values
@property (nonatomic, readonly) double meanRssi;
@property (nonatomic, readonly) double stdDeviationRssi;
// Calculated range distances based on above
@property (nonatomic, readonly) double meters;
@property (nonatomic, readonly) double meanMeters;
@property (nonatomic, readonly) double meanMetersVariance;


+ (CDSBeaconLandmark*)landmarkWithIdent:(NSString*)ident x:(double)x y:(double)y color:(UIColor*) color;

// Helpers
+ (NSString*)identFromMajor:(NSInteger)major minor:(NSInteger)minor;

@end
