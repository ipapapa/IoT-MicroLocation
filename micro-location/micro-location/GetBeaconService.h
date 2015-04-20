//
//  GetBeaconService.h
//  micro-location
//
//  Created by Ian Hamilton on 3/13/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import "AFHTTPClient.h"

@protocol GetBeaconServiceDelegate <NSObject>

-(void) getBeaconReturnedSuccessfully:(NSDictionary *)response;
-(void) getBeaconFailedWithError:(NSError *) error;

@end

@interface GetBeaconService : AFHTTPClient
+(GetBeaconService*) sharedClient;
-(NSError*) getBeaconWithUUID:(NSDictionary*) uuid;

@property (nonatomic, weak) id <GetBeaconServiceDelegate> mGetBeaconServiceDelegate;

@end
