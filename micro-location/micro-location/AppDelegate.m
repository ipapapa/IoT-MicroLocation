//
//  AppDelegate.m
//  micro-location
//
//  Created by Ian Hamilton on 2/9/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import "AppDelegate.h"
#import "ProximityViewController.h"
#import "Singleton.h"
#import "iBeacon.h"
#import "AFNetworking.h"
#import "DDASLLogger.h"
#import "DDTTYLogger.h"

#import <CoreLocation/CoreLocation.h>
#import <CoreBluetooth/CoreBluetooth.h>


@interface AppDelegate ()

@property (nonatomic, strong) NSUUID *beaconUUID;

@end

@implementation AppDelegate



- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    //self.beaconUUID = [[NSUUID alloc] initWithUUIDString:@"F4913F46-75F4-9134-913F-4913F4913F49"]; //gimbal uuid
    self.beaconUUID = [[NSUUID alloc] initWithUUIDString:@"B9407F30-F5F8-466E-AFF9-25556B57FE6D"];  //estimote uuid
    [DDLog addLogger:[DDASLLogger sharedInstance]];
    [DDLog addLogger:[DDTTYLogger sharedInstance]];
    
    NSString *regionIdentifier = @"12345";
    CLBeaconRegion *beaconRegion = [[CLBeaconRegion alloc] initWithProximityUUID:self.beaconUUID identifier:regionIdentifier];
    
    switch ([CLLocationManager authorizationStatus])
    {
        case kCLAuthorizationStatusAuthorizedAlways:
            NSLog(@"Authorized Always");
            break;
        case kCLAuthorizationStatusAuthorizedWhenInUse:
            NSLog(@"Authorized when in use");
            break;
        case kCLAuthorizationStatusDenied:
            NSLog(@"Denied");
            break;
        case kCLAuthorizationStatusNotDetermined:
            NSLog(@"Not determined");
            break;
        case kCLAuthorizationStatusRestricted:
            NSLog(@"Restricted");
            break;
            
        default:
            break;
    }
    
    [Singleton instance];
    [Singleton instance].knownBeacons = [NSMutableArray array];
    
    self.locationManager = [[CLLocationManager alloc] init];
    
    if([self.locationManager respondsToSelector:@selector(requestAlwaysAuthorization)])
    {
        [self.locationManager requestAlwaysAuthorization];
    }
    self.locationManager.delegate = self;
    self.locationManager.pausesLocationUpdatesAutomatically = NO;
    [self.locationManager startMonitoringForRegion:beaconRegion];
    
    [self.locationManager startRangingBeaconsInRegion:beaconRegion];
    [self.locationManager startUpdatingLocation];
    
    return YES;
}

-(void)locationManager:(CLLocationManager *)manager didEnterRegion:(CLRegion *)region
{
    [manager startRangingBeaconsInRegion:(CLBeaconRegion *)region];
    [self.locationManager startUpdatingLocation];
    
    NSLog(@"You entered the region.");
    [self sendLocalNotificationWithMessage:@"You entered the region."];
}

-(void)locationManager:(CLLocationManager *)manager didExitRegion:(CLRegion *)region
{
    [manager stopRangingBeaconsInRegion:(CLBeaconRegion *)region];
    [self.locationManager stopUpdatingLocation];
    
    NSLog(@"You exited the region.");
    [self sendLocalNotificationWithMessage:@"You exited the region."];
}

-(void)sendLocalNotificationWithMessage:(NSString*)message
{
    UILocalNotification *notification = [[UILocalNotification alloc] init];
    notification.alertBody = message;
    [[UIApplication sharedApplication] scheduleLocalNotification:notification];
}


-(void)locationManager:(CLLocationManager *)manager didRangeBeacons:(NSArray *)beacons inRegion:(CLBeaconRegion *)region
{
    NSString *message = @"";
    BOOL isKnownBeacon = false;
    
    [Singleton instance].beacons = beacons;
    [[Singleton instance].tableView reloadData];
    [[Singleton instance].geoTableView reloadData];
    
    NSString *urlString = @"10.10.10.10:8080/microlocation";
    
    for (CLBeacon *beacon in [Singleton instance].beacons)
    {
        NSString *uuid = [NSString stringWithFormat:@"%@",beacon.proximityUUID];
        NSString *major = [beacon.major stringValue];
        NSString *minor = [beacon.minor stringValue];
        
        NSDictionary *params = @ {@"uuid" :uuid, @"major" :major, @"minor" :minor };
        GetBeaconService *beaconService = [GetBeaconService sharedClient];
        beaconService.mGetBeaconServiceDelegate = self;
        
        if ([Singleton instance].knownBeacons.count == 0)
        {
            if (beaconService)
            {
                NSError *error = [beaconService getBeaconWithUUID:params];
            }
        }
        else
        {
            for (iBeacon *knownBeacon in [Singleton instance].knownBeacons)
            {
                if ([knownBeacon.major isEqualToString:major] && [knownBeacon.minor isEqualToString:minor])
                {
                    isKnownBeacon = true;
                }
            }
            
            if (!isKnownBeacon)
            {
                if (beaconService)
                {
                    NSError *error = [beaconService getBeaconWithUUID:params];
                }
            }
        }
    }
    
        NSString *uuid = [NSString stringWithFormat:@"%@",self.beaconUUID];
        NSNumber *major = beacon.major;
        NSNumber *minor = beacon.minor;
        
        AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
        manager.requestSerializer = [AFJSONRequestSerializer serializer];
        
        
        NSDictionary *params = @ {@"uuid" :uuid, @"major" :major, @"minor" :minor };
        
        [manager POST:urlString parameters:params
              success:^(AFHTTPRequestOperation *operation, id responseObject)
         {
             NSLog(@"JSON: %@", responseObject);
         }
              failure:^(AFHTTPRequestOperation *operation, NSError *error)
         {
             NSLog(@"Error: %@", error);
         }];
    }
    
    if(beacons.count > 0)
    {
        CLBeacon *nearestBeacon = beacons.firstObject;
        
        if(nearestBeacon.proximity == self.lastProximity || nearestBeacon.proximity == CLProximityUnknown)
        {
            return;
        }
        self.lastProximity = nearestBeacon.proximity;
        
        switch(nearestBeacon.proximity)
        {
            case CLProximityFar:
                message = @"You are far away from the beacon";
                break;
            case CLProximityNear:
                message = @"You are near the beacon";
                break;
            case CLProximityImmediate:
                message = @"You are in the immediate proximity of the beacon";
                break;
            case CLProximityUnknown:
                return;
        }
    }
    else
    {
        message = @"No beacons are nearby";
    }
    
    
    
    NSLog(@"%@", message);
    //[self sendLocalNotificationWithMessage:message];
}

- (void)getBeaconReturnedSuccessfully:(NSDictionary *)response
{
    NSLog(@"Response: %@", response);
    BOOL isKnownBeacon = false;
    
    iBeacon *sourcedBeacon = [[iBeacon alloc]initWithDict:response];
    
    if ([Singleton instance].knownBeacons.count == 0)
    {
        [[Singleton instance].knownBeacons addObject:sourcedBeacon];
    }
    else
    {
        for (iBeacon *knownBeacon in [Singleton instance].knownBeacons)
        {
            if ([knownBeacon.major isEqualToString:sourcedBeacon.major] &&
                [knownBeacon.minor isEqualToString:sourcedBeacon.minor])
            {
                isKnownBeacon = true;
            }
        }
        
        if (!isKnownBeacon)
        {
            [[Singleton instance].knownBeacons addObject:sourcedBeacon];
        }
        
        NSLog(@"Known Beacons: %lu", (unsigned long)[Singleton instance].knownBeacons.count);
    }
}

- (void)getBeaconFailedWithError:(NSError *)error
{
    NSLog(@"Failure: %@", error);
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end

