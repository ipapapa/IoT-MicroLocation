//
//  iBeacon.m
//  micro-location
//
//  Created by Ian Hamilton on 4/16/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import "iBeacon.h"

@implementation iBeacon

- (id)initWithDict:(NSDictionary *)dict
{
    self = [super init];
    
    if(!dict || dict.count <= 0)
    {
        
        //[NSError logErrorWithCode:TRRShopNullParameterError andMessage:@"Address initWithDict:"];
        return self;
    }
    
    self.name = [dict valueForKey:@"name"];
    self.uuid = [dict valueForKey:@"uuid"];
    self.major = [dict valueForKey:@"major"];
    self.minor = [dict valueForKey:@"minor"];
    self.usecase = [dict valueForKey:@"usecase"];
    
    return self;
    
}

- (NSDictionary*)toDict
{
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    dict[@"name"] = self.name;
    dict[@"uuid"] = self.uuid;
    dict[@"major"] = self.major;
    dict[@"minor"] = self.minor;
    dict[@"usecase"] = self.usecase;
    
    return dict;
}

- (BOOL)isEqual:(id)object
{
    if ([object isKindOfClass:[iBeacon class]])
    {
        iBeacon *iBeacon = object;
        return [self.major isEqualToString:iBeacon.major] &&
                [self.minor isEqualToString:iBeacon.minor] &&
                [self.name isEqualToString:iBeacon.name];
    }
    
    return NO;
}


@end
