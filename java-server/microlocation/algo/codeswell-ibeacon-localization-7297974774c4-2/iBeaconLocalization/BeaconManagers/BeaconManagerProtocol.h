//
//  BeaconManagerProtocol.h
//  iBeaconLocalization
//
//  Created by Andrew Craze on 3/2/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#ifndef iBeaconLocalization_BeaconManagerProtocol_h
#define iBeaconLocalization_BeaconManagerProtocol_h


@protocol BeaconManagerDelegate <NSObject>

- (void)rangedBeacons:(NSArray*)beacons;

@end


@protocol BeaconManager <NSObject>

@property (nonatomic, weak) id <BeaconManagerDelegate> delegate;

- (instancetype)initWithProximityUuid:(NSUUID*)proximityUuid regionId:(NSString*)beaconRegionId;
- (void)beginRanging;
- (void)stopRanging;

@end


#endif
