//
//  SecondViewController.h
//  micro-location
//
//  Created by Ian Hamilton on 2/9/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SecondViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>

@property (strong) NSArray *beacons;
@property (strong) UIImage *beaconImage;

@end


