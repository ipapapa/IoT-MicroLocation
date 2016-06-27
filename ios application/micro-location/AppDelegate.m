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
    self.beaconUUID = [[NSUUID alloc] initWithUUIDString:@"F4913F46-75F4-9134-913F-4913F4913F49"];//9"]; //gimbal uuid // the beacon UUID is declared
    //self.beaconUUID = [[NSUUID alloc] initWithUUIDString:@"B9407F30-F5F8-466E-AFF9-25556B57FE6D"];  //estimote uuid

    [DDLog addLogger:[DDASLLogger sharedInstance]];
    [DDLog addLogger:[DDTTYLogger sharedInstance]];
    
    NSString *regionIdentifier = @"12345";  //region id that we have for the specific region we are monitoring, Apple allows monitoring about 20 regions simultaneously by an App.
    CLBeaconRegion *beaconRegion = [[CLBeaconRegion alloc] initWithProximityUUID:self.beaconUUID identifier:regionIdentifier];
    
    switch ([CLLocationManager authorizationStatus]) // authorization status to be pedicted
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
    [Singleton instance].hasCalledWemoScript = false;
    
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
    BOOL isKnownBeacon = false;
    
    [Singleton instance].beacons = beacons;
    [[Singleton instance].tableView reloadData];
    [[Singleton instance].geoTableView reloadData];
    
    for (CLBeacon *beacon in [Singleton instance].beacons)
    {
        NSString *noFormatUUID = [NSString stringWithFormat:@"%@",beacon.proximityUUID];
        NSArray *uuidFormatArray = [noFormatUUID componentsSeparatedByString:@"> "];
        NSString *formatUUID = uuidFormatArray[1];
        
      //  NSLog(@"%@",formatUUID);
      //  NSLog(@"%@",noFormatUUID);
        NSString *major = [beacon.major stringValue];
        NSString *minor = [beacon.minor stringValue];
        
        NSDictionary *params = @ {@"uuid" :formatUUID, @"major" :major, @"minor" :minor };
        NSString *deviceId=[[UIDevice currentDevice] model];
        NSDictionary *params2 = @ {@"uuid" :formatUUID, @"major" :major, @"minor" :minor, @"deviceId":deviceId };
      
        GetBeaconService *beaconService = [GetBeaconService sharedClient];
        beaconService.mGetBeaconServiceDelegate = self;
        
        if ([Singleton instance].knownBeacons.count == 0 )
        {
            if (beaconService)
            {
                NSError *error = [beaconService getBeaconWithUUID:params];
                // the function in GetBeaconService is called and the parameters are passed 
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
}


- (void)getBeaconReturnedSuccessfully:(NSDictionary *)response
{
   // NSLog(@"Response: %@", response);
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
        
     //   NSLog(@"Known Beacons: %lu", (unsigned long)[Singleton instance].knownBeacons.count);
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

