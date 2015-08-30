//
//  BeaconManagerProtocol.h
//  micro-location
//
//  Created by Fahim Zafari on 2/18/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
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
