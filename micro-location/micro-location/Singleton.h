//
//  Singleton.h
//  micro-location
//
//  Created by Ian Hamilton on 2/9/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface Singleton : NSObject

@property (nonatomic, strong) NSArray *beacons;
@property (nonatomic, strong) NSArray *geoBeacons;
@property (nonatomic, strong) UITableView *tableView;
@property (nonatomic, strong) UITableView *geoTableView;

+ (Singleton *)instance;

@end
