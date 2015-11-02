//
//  GenericBeaconManager.h
//  micro-location
//
//  Created by Fahim Zafari on 2/18/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//


#import <Foundation/Foundation.h>
#import "BeaconManagerProtocol.h"

@interface GenericBeaconManager : NSObject <BeaconManager>

@property (nonatomic, weak) id <BeaconManagerDelegate> delegate;

@end
