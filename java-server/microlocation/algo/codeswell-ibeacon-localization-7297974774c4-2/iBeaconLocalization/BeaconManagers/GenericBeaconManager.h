//
//  GenericBeaconManager.h
//  iBeaconLocalization
//
//  Created by Andrew Craze on 3/2/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BeaconManagerProtocol.h"

@interface GenericBeaconManager : NSObject <BeaconManager>

@property (nonatomic, weak) id <BeaconManagerDelegate> delegate;

@end
