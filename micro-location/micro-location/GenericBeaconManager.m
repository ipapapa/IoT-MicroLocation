//
//  GenericBeaconManager.m
//  micro-location
//
//  Created by Fahim Zafari on 2/18/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import <CoreLocation/CoreLocation.h>
#import "GenericBeaconManager.h"
#import "DDLog.h"
#import "DDASLLogger.h"
#import "DDTTYLogger.h"
#import "DDLog.h"

@interface GenericBeaconManager () <CLLocationManagerDelegate>

@property (nonatomic, strong) NSString* beaconRegionId;
@property (nonatomic, strong) CLBeaconRegion* region;
@property (nonatomic, strong) CLLocationManager* locationMgr;

@end


@implementation GenericBeaconManager

- (instancetype)initWithProximityUuid:(NSUUID *)proximityUuid regionId:(NSString *)beaconRegionId {
    
    if ((self = [super init])) {
        _beaconRegionId = beaconRegionId;
        
        _locationMgr = [[CLLocationManager alloc] init];
        _locationMgr.delegate = self;
        
        _region = [[CLBeaconRegion alloc]
                   initWithProximityUUID:proximityUuid
                   identifier:beaconRegionId];
    }
    return self;
}

- (void)beginRanging {
    [self.locationMgr startRangingBeaconsInRegion:self.region];
}

- (void)stopRanging {
    [self.locationMgr stopRangingBeaconsInRegion:self.region];
}


#pragma mark CLLocationManagerDelegate protocol


- (void)locationManager:(CLLocationManager *)manager
        didRangeBeacons:(NSArray *)beacons
               inRegion:(CLBeaconRegion *)region
{
    
    
    if ([region.identifier isEqualToString:self.beaconRegionId]) {
//                for (CLBeacon* beacon in beacons) {
//                    beacon.rssi;
//                    beacon.major;
//                    beacon.minor;
//                }
        
        [self.delegate rangedBeacons:beacons];
    }
}


- (void)locationManager:(CLLocationManager *)manager rangingBeaconsDidFailForRegion:(CLBeaconRegion *)region withError:(NSError *)error {
//    DDLogError(@"[%@] Ranging failed: %@", self.class, [error localizedDescription]);
}


@end
