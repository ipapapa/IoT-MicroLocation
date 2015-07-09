//
//  GenericBeaconManager.m
//  iBeaconLocalization
//
//  Created by Andrew Craze on 3/2/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#import <CoreLocation/CoreLocation.h>
#import "GenericBeaconManager.h"

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
//        for (CLBeacon* beacon in beacons) {
//            beacon.rssi;
//            beacon.major;
//            beacon.minor;
//        }
        
        [self.delegate rangedBeacons:beacons];
    }
}


- (void)locationManager:(CLLocationManager *)manager rangingBeaconsDidFailForRegion:(CLBeaconRegion *)region withError:(NSError *)error {
    DDLogError(@"[%@] Ranging failed: %@", self.class, [error localizedDescription]);
}


@end
