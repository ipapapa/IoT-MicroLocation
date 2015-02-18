//
//  ProximityViewController.h
//  micro-location
//
//  Created by Ian Hamilton on 2/9/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>

@interface ProximityViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UIApplicationDelegate>

@property (strong) UIImage *beaconImage;
@property (strong) UIImage *sel_beaconImage;

@end
