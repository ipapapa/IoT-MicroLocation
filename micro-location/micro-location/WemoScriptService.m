//
//  WemoScriptService.m
//  micro-location
//
//  Created by Ian Hamilton on 4/29/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import "WemoScriptService.h"

@implementation WemoScriptService
@synthesize mWemoScriptServiceDelegate;

- (id) initWithBaseURL:(NSURL *)url
{
    self = [super initWithBaseURL:url];
    if (!self) {
        return nil;
    }
    
    //[self registerHTTPOperationClass:[AFJSONRequestOperation class]];
    //[self setDefaultHeader:@"Accept" value:@"application/json"];
    
    return self;
}

+ (WemoScriptService *)sharedClient {
    static WemoScriptService* _sharedClient = nil;
    static dispatch_once_t oncePredicate;
    dispatch_once(&oncePredicate, ^{
        _sharedClient = [[self alloc] initWithBaseURL:[NSURL URLWithString:@"http://10.184.1.242:8080"]];
    });
    
    return _sharedClient;
}

- (NSError *)runWemoScript
{
    NSString *getPath = @"/microlocation/WemoController";
    
    [self getPath:getPath
       parameters:nil
          success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         
     }
     failure:^(AFHTTPRequestOperation *operation, NSError *error)
     {
         NSLog(@"Error = %@", error);
     }];
    
    return [NSError errorWithDomain:@"" code:100 userInfo:nil];
}

@end
