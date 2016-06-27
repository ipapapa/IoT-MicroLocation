//
//  WemoScriptService2.m
//  micro-location
//
//  Created by Fahim Zafari on 7/21/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import "WemoScriptService2.h"
#import "CDSiBeaconPFViewController.h"
@implementation WemoScriptService2



@synthesize mWemoScriptService2Delegate;

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

+ (WemoScriptService2 *)sharedClient {
    static WemoScriptService2* _sharedClient = nil;
    static dispatch_once_t oncePredicate;
    dispatch_once(&oncePredicate, ^{
        _sharedClient = [[self alloc] initWithBaseURL:[NSURL URLWithString:@"http://10.186.151.103:8080/microlocation"]];// local
        
        
        //    _sharedClient = [[self alloc] initWithBaseURL:[NSURL URLWithString:@"http://microlocation.elasticbeanstalk.com/"]]; // amazon aws
    });
    
    return _sharedClient;
}

- (NSError *)runWemoScript2withParams:(NSDictionary *)params
{
    
    
    NSString *postPath = @"WemoController4";
    
    [self postPath:postPath parameters:params
           success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         
     }
           failure:^(AFHTTPRequestOperation *operation, NSError *error)
     {
         NSLog(@"Error = %@", error);
     }];
    
    return [NSError errorWithDomain:@"" code:100 userInfo:nil];
}

//- (NSError *)runWemoScript2withParams:(NSDictionary *)params
//{
//    // NSLog(@"%@",uuid);
//    NSString* postPath = @"WemoController3";
//    [self postPath:postPath
//        parameters:params
//           success:^(AFHTTPRequestOperation *operation, id responseObject)
//     {
//         if(self.mWemoScriptService2Delegate &&
//            [self.mWemoScriptService2Delegate respondsToSelector: @selector(getParticleReturnedSuccessfully:)])
//         {
//             [self.mWemoScriptService2Delegate getParticleReturnedSuccessfully:responseObject];
//         }
//         
//         //NSLog(@"Response = %@", responseObject);
//         
//     }
//           failure:^(AFHTTPRequestOperation *operation, NSError *error) {
//               if(self.mWemoScriptService2Delegate &&
//                  [self.mWemoScriptService2Delegate respondsToSelector:@selector(getParticleFailedWithError:)])
//                   [self.mWemoScriptService2Delegate getParticleFailedWithError:error];
//               
//               //NSLog(@"Request = %@", operation);
//               NSLog(@"Error = %@", error);
//           }];
//    
//    //return [NSError errorWithCode:TRRShopErrorNone andMessage:@"Success"];
//    return [NSError errorWithDomain:@"" code:100 userInfo:nil];
//}

@end
