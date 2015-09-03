//
//  WemoScriptService2.h
//  micro-location
//
//  Created by Fahim Zafari on 7/21/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "AFHTTPClient.h"

@protocol WemoScriptServiceDelegate2 <NSObject>

-(void) wemoScriptRanSuccessfully2;
-(void) wemoScriptRanWithError2:(NSError *) error;

// newly added
//-(void) getParticleReturnedSuccessfully:(NSDictionary *)response;
//-(void) getParticleFailedWithError:(NSError *) error;

@end

@interface WemoScriptService2 : AFHTTPClient
+(WemoScriptService2*) sharedClient;
- (NSError *)runWemoScript2withParams:(NSDictionary *)params;

@property (nonatomic, weak) id <WemoScriptServiceDelegate2> mWemoScriptService2Delegate;

@end