//
//  GetBeaconService.m
//  micro-location
//
//  Created by Ian Hamilton on 3/13/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import "GetBeaconService.h"
#import "AFJSONRequestOperation.h"

@implementation GetBeaconService
@synthesize mGetBeaconServiceDelegate;

- (id) initWithBaseURL:(NSURL *)url
{
    self = [super initWithBaseURL:url];
    if (!self) {
        return nil;
    }
    
    [self registerHTTPOperationClass:[AFJSONRequestOperation class]];
    [self setDefaultHeader:@"Accept" value:@"application/json"];
    
    return self;
}

+ (GetBeaconService *)sharedClient {
    static GetBeaconService* _sharedClient = nil;
    static dispatch_once_t oncePredicate;
    dispatch_once(&oncePredicate, ^{
     //   _sharedClient = [[self alloc] initWithBaseURL:[NSURL URLWithString:@"http://microlocationsau-env.elasticbeanstalk.com"]];
        _sharedClient = [[self alloc] initWithBaseURL:[NSURL URLWithString:@"http://10.186.151.103:8080/microlocation"]]; //local
        
    //   _sharedClient = [[self alloc] initWithBaseURL:[NSURL URLWithString:@"http://microlocation.elasticbeanstalk.com/"]];
    });
    
    return _sharedClient;
    
}

-(NSError*) getBeaconWithUUID:(NSDictionary *)uuid
{
   // NSLog(@"%@",uuid);
    NSString* postPath = @"BeaconController";
    [self postPath:postPath
       parameters:uuid
           success:^(AFHTTPRequestOperation *operation, id responseObject)
            {
                if(self.mGetBeaconServiceDelegate &&
                   [self.mGetBeaconServiceDelegate respondsToSelector: @selector(getBeaconReturnedSuccessfully:)])
                {
                    [self.mGetBeaconServiceDelegate getBeaconReturnedSuccessfully:responseObject];
                }
                       
                //NSLog(@"Response = %@", responseObject);
                
           }
           failure:^(AFHTTPRequestOperation *operation, NSError *error) {
               if(self.mGetBeaconServiceDelegate &&
                  [self.mGetBeaconServiceDelegate respondsToSelector:@selector(getBeaconFailedWithError:)])
                   [self.mGetBeaconServiceDelegate getBeaconFailedWithError:error];
               
               //NSLog(@"Request = %@", operation);
               NSLog(@"Error = %@", error);
           }];
    
    //return [NSError errorWithCode:TRRShopErrorNone andMessage:@"Success"];
    return [NSError errorWithDomain:@"" code:100 userInfo:nil];
}

@end
