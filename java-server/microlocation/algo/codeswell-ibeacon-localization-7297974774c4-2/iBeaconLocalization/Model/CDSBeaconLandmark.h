//
//  CDSBeaconLandmark.h
//  iBeaconLocalization
//
//  Created by Andrew Craze on 12/12/13.
//  Copyright (c) 2013 Codeswell. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CDSBeaconLandmark : NSObject

@property (nonatomic, readonly) NSString* ident;
@property (nonatomic, readonly) double x;
@property (nonatomic, readonly) double y;
@property (nonatomic, readonly) UIColor* color;

@property (nonatomic, assign) NSInteger rssi;
// Trailing average and standard deviation of RSSI values
@property (nonatomic, readonly) double meanRssi;
@property (nonatomic, readonly) double stdDeviationRssi;
// Calculated range distances based on above
@property (nonatomic, readonly) double meters;
@property (nonatomic, readonly) double meanMeters;
@property (nonatomic, readonly) double meanMetersVariance;


+ (CDSBeaconLandmark*)landmarkWithIdent:(NSString*)ident x:(double)x y:(double)y color:(UIColor*)color;

// Helpers
+ (NSString*)identFromMajor:(NSInteger)major minor:(NSInteger)minor;

@end
