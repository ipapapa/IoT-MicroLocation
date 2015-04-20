//
//  iBeacon.h
//  micro-location
//
//  Created by Ian Hamilton on 4/16/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface iBeacon : NSObject

@property (nonatomic, strong) NSString *uuid;
@property (nonatomic, strong) NSString *major;
@property (nonatomic, strong) NSString *minor;
@property (nonatomic, strong) NSString *name;
@property (nonatomic, strong) NSString *usecase;

- (id)initWithDict:(NSDictionary *)dict;
- (NSDictionary*)toDict;

@end
