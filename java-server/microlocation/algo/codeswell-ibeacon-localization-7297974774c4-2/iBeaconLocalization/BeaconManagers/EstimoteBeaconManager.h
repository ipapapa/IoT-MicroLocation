//
//  EstimoteBeaconManager.h
//  iBeaconLocalization
//
//  Created by Andrew Craze on 3/1/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BeaconManagerProtocol.h"


@interface EstimoteBeaconManager : NSObject <BeaconManager>

@property (nonatomic, weak) id <BeaconManagerDelegate> delegate;

@end
