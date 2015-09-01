//
//  Singleton.h
//  micro-location
//
//  Created by Ian Hamilton on 2/9/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "iBeacon.h"

@interface Singleton : NSObject

@property (nonatomic, strong) NSArray *beacons;
@property (nonatomic, strong) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NSMutableArray *knownBeacons;
@property (nonatomic, strong) NSMutableArray *geoBeacons;
@property (nonatomic, strong) UITableView *geoTableView;
@property (nonatomic) BOOL hasCalledWemoScript;
@property (nonatomic) BOOL hasCalledWemoScript2;
@property (nonatomic) BOOL hasCalledWemoScript3;
@property (nonatomic) BOOL hasCalledWemoScript4;
@property (nonatomic) BOOL hasCalledWemoScript5;
+ (Singleton *)instance;

@end
