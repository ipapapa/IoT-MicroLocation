//
//  EstimoteBeaconManager.m
//  iBeaconLocalization
//
//  Created by Andrew Craze on 3/1/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#import "EstimoteBeaconManager.h"
#import "ESTBeaconManager.h"


@interface EstimoteBeaconManager () <ESTBeaconManagerDelegate>

@property (nonatomic, strong) NSString* beaconRegionId;
@property (nonatomic, strong) ESTBeaconRegion* region;
@property (nonatomic, strong) ESTBeaconManager* beaconMgr;

@end


@implementation EstimoteBeaconManager


- (instancetype)initWithProximityUuid:(NSUUID*)proximityUuid regionId:(NSString*)beaconRegionId {
    if ((self = [super init])) {
        _beaconRegionId = beaconRegionId;
        
        _beaconMgr = [[ESTBeaconManager alloc] init];
        _beaconMgr.delegate = self;
        _beaconMgr.avoidUnknownStateBeacons = YES;
        
        _region = [[ESTBeaconRegion alloc] initWithProximityUUID:proximityUuid
                                                                      identifier:beaconRegionId];
    }
    return self;
}


- (void)beginRanging {
    [self.beaconMgr startRangingBeaconsInRegion:self.region];
}


- (void)stopRanging {
    [self.beaconMgr stopRangingBeaconsInRegion:self.region];
}


#pragma mark ESTBeaconManagerDelegate protocol

- (void)beaconManager:(ESTBeaconManager *)manager
     didRangeBeacons:(NSArray *)beacons
            inRegion:(ESTBeaconRegion *)region
{
    if ([region.identifier isEqualToString:self.beaconRegionId]) {
        // Estimote provides richer information than the generic CLBeacon
        // REVIEW: take advantage of the richer Estimote information
//        for (ESTBeacon* beacon in beacons) {
//            beacon.rssi;
//            beacon.major;
//            beacon.minor;
//            beacon.measuredPower;
//            beacon.distance;
//            beacon.peripheral.RSSI;
//        }
        
        [self.delegate rangedBeacons:beacons];
    }
}

- (void)beaconManager:(ESTBeaconManager *)manager rangingBeaconsDidFailForRegion:(ESTBeaconRegion *)region
            withError:(NSError *)error
{
    DDLogError(@"[%@] Ranging failed: %@", self.class, [error localizedDescription]);
}


@end
