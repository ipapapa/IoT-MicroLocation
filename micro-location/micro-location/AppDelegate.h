//
//  AppDelegate.h
//  micro-location
//
//  Created by Ian Hamilton on 2/9/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import "Singleton.h"

@interface AppDelegate : UIResponder <CLLocationManagerDelegate>//,UITabBarControllerDelegate,UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) CLLocationManager *locationManager;
@property CLProximity lastProximity;
@end

