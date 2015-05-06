//
//  WemoScriptService.h
//  micro-location
//
//  Created by Ian Hamilton on 4/29/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import "AFHTTPClient.h"

@protocol WemoScriptServiceDelegate <NSObject>

-(void) wemoScriptRanSuccessfully;
-(void) wemoScriptRanWithError:(NSError *) error;

@end

@interface WemoScriptService : AFHTTPClient
+(WemoScriptService*) sharedClient;
- (NSError *)runWemoScriptwithParams:(NSDictionary *)params

@property (nonatomic, weak) id <WemoScriptServiceDelegate> mWemoScriptServiceDelegate;

@end
