//
//  SecondViewController.m
//  micro-location
//
//  Created by Ian Hamilton on 2/9/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import "SecondViewController.h"
#import "Singleton.h"
#import "AFNetworking.h"
#import "iBeacon.h"
#import <CoreLocation/CoreLocation.h>
#import "AppDelegate.h"
@interface SecondViewController ()


@end

@implementation SecondViewController
BOOL artw1=true;
BOOL artw2=true;

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    float height = self.view.bounds.size.height; // the code is for the view of the tab which in our case consists of tables
    float width = self.view.bounds.size.width;
    
    CGRect tableFrame = CGRectMake(0, 20, width,height);//568, 375);
    [Singleton instance].geoTableView = [[UITableView alloc]initWithFrame:tableFrame];
    [Singleton instance].geoTableView.allowsMultipleSelection = NO;
    [Singleton instance].geoTableView.delegate = self;
    [Singleton instance].geoTableView.dataSource = self;
    
    [self.view addSubview:[Singleton instance].geoTableView];
}
/*
 The following method is for creating beacon arrays which will show the beacons for a particular task 
 Let us add a method for the task x 
 then it should be 
 - (NSMutableArray *)createxBeaconsArray  // create an array for the a specific beacon type
 {
 NSMutableArray *xBeaconsArray = [NSMutableArray array];
 
 for (iBeacon *beacon in [Singleton instance].knownBeacons)
 {
 if ([beacon.usecase isEqualToString:@"x"])
 {
 [xBeaconsArray addObject:beacon];
 }
 }
 
 return xBeaconsArray;
 }
 similary we have to modify the numberOfSectionsInTableView and it should return 3 rather than 2 as it has 3 rows now
 */
- (NSMutableArray *)createGeoBeaconsArray  // create an array for the a specific beacon type
{
    NSMutableArray *geoBeaconsArray = [NSMutableArray array];
    
    for (iBeacon *beacon in [Singleton instance].knownBeacons)
    {
        if ([beacon.usecase isEqualToString:@"GEO"])
        {
            [geoBeaconsArray addObject:beacon];
        }
    }
    
    return geoBeaconsArray;
}

- (NSMutableArray *)createOtherBeaconsArray
{
    NSMutableArray *otherBeaconsArray = [NSMutableArray array];
    
    for (iBeacon *beacon in [Singleton instance].knownBeacons)
    {
        if (![beacon.usecase isEqualToString:@"GEO"])
        {
            [otherBeaconsArray addObject:beacon];
        }
    }
    
    return otherBeaconsArray;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView  // for the number of sections in our application table view
{
    return 2;
    // return 3; // for xbeacon
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section  // title for the table
{
    NSString *title = @"";
    if (section == 0)
        title = @"Geofencing Beacons";
    else if (section == 1)
        title = @"Other Beacons";
    //else if (section ==2)
      //  title = @"X beacons";
    return title;
}

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section  // number of rows in a section
// the number of rows will be 1 if there are no beacons and incase if there are beacons, the number of rows will be equal to the
// the number of beacons
{
    NSUInteger numRows = 0;
    
    if (section == 0)
    {
        if ([Singleton instance].knownBeacons.count == 0)
            numRows = 1;
        else
        {
            NSMutableArray *geoBeaconsArray = [self createGeoBeaconsArray];
            numRows = geoBeaconsArray.count;
        }
    }
    else if (section == 1)
    {
        if ([Singleton instance].knownBeacons.count == 0)
            numRows = 1;
        else
		{
			NSMutableArray *otherBeaconsArray = [self createOtherBeaconsArray];
			numRows = otherBeaconsArray.count;
		}
    }

/*    else if (section  == 2)
    {
        if([Singleton instance].knownBeacons.count==0)
            numRows = 1;
        else
        {
                NSMutableArray *xBeaconsArray =[self xBeaconsArray];
                numRows = xBeaconsArray.count;
        }
 
    }*/
    return numRows;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MyIdentifier"];
    
    if (cell == nil)
    {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:@"MyIdentifier"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.textLabel.font = [UIFont fontWithName:@"Helvetica" size:10.0];
        cell.textLabel.lineBreakMode = NSLineBreakByWordWrapping; // Pre-iOS6 use UILineBreakModeWordWrap
        cell.textLabel.numberOfLines = 4;  // 0 means no max.
    }
    
    NSMutableArray *geoBeaconsArray = [self createGeoBeaconsArray];
    NSMutableArray *otherBeaconsArray = [self createOtherBeaconsArray];
    NSString *beaconName = @"No beacons of this kind were found.";
    
  
    if (indexPath.section == 0)
    {
        
        if (geoBeaconsArray.count > 0)
        {
            iBeacon *beacon = (iBeacon *)[geoBeaconsArray objectAtIndex:indexPath.row];
            beaconName = beacon.name;
            cell.detailTextLabel.text = [NSString stringWithFormat:@"Major: %@ | Minor: %@", beacon.major, beacon.minor];
            

//            CLBeacon *thisCLBeacon = nil;
//          
//            for (CLBeacon *clbeacon in [Singleton instance].beacons)
//            {
//                if ([[clbeacon.major stringValue] isEqualToString:beacon.major] && [[clbeacon.minor stringValue] isEqualToString:beacon.minor] )
//                {
//                    
//                    thisCLBeacon = clbeacon;
//                  
//                }
//            }
          
            
            //test
            CLBeacon *thisCLBeaconartw1 = nil;
            CLBeacon *thisCLBeaconartw2 = nil;
            // NSArray *test=[Singleton instance].beacons;
            //  int size=[test count];
            
            //    NSLog(@"The beacons are %d ",size);
            for (CLBeacon *clbeacon in [Singleton instance].beacons)
            {
                if ([[clbeacon.major stringValue] isEqualToString:beacon.major] && [[clbeacon.minor stringValue] isEqualToString:beacon.minor] && [beacon.name isEqualToString:@"artw"] && [beacon.minor isEqualToString:@"1"] )
                {
                    
                    thisCLBeaconartw1 = clbeacon;
            //        NSLog(@"Beacon 1 is %@ ",thisCLBeaconartw1);
                }
                if ([[clbeacon.major stringValue] isEqualToString:beacon.major] && [[clbeacon.minor stringValue] isEqualToString:beacon.minor] && [beacon.name isEqualToString:@"artw"] && [beacon.minor isEqualToString:@"2"] )
                {
                    
                    thisCLBeaconartw2 = clbeacon;
                //    NSLog(@"Beacon 2 is %@ ",thisCLBeaconartw2);
                }
                
            }

            if([beacon.minor isEqualToString:@"1"])
            {
                if((thisCLBeaconartw1.proximity == CLProximityImmediate || thisCLBeaconartw1.proximity==CLProximityNear))
                {
                    artw1=true;
                }
                else if((thisCLBeaconartw1.proximity == CLProximityFar || thisCLBeaconartw1.proximity==CLProximityUnknown))
                {
                    artw1=false;
                }
            }
            if([beacon.minor isEqualToString:@"2"])
            {
                if((thisCLBeaconartw2.proximity == CLProximityImmediate || thisCLBeaconartw2.proximity==CLProximityNear))
                {
                    artw2=true;
                }
                else if((thisCLBeaconartw2.proximity == CLProximityFar || thisCLBeaconartw2.proximity==CLProximityUnknown))
                {
                    artw2=false;
                }
            }
   //         NSLog(@"artw 2 is %d",artw2);
    //         NSLog(@"artw 1 is %d",artw1);

            
            
            //CLBeacon *clbeacon = (CLBeacon *)[Singleton instance].beacons objectAtIndex:;]
            
            {
            WemoScriptService *wemoService = [WemoScriptService sharedClient];
            wemoService.mWemoScriptServiceDelegate = self;
            
            NSString *onOffString = @"";
            NSDictionary *params = @ {};
            NSDictionary *params2 = @ {};
            NSString *deviceId=[[UIDevice currentDevice] model];
            
            if (([beacon.name isEqualToString:@"artw"] ) &&  ![Singleton instance].hasCalledWemoScript   && (artw1 || artw2))
            {
                //if you are in immediate region the script of turning the switch on will be executed
                onOffString = @"on";
                params = @ {@"onoroff" : onOffString};
               // params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"uuid" :beacon.uuid, @"major" :beacon.major, @"minor" :beacon.minor};//
                params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"name":beacon.name};
                NSLog(@"params are for artw %@",params);
               // NSError *error =
                [wemoService runWemoScriptwithParams:params2];//
                [Singleton instance].hasCalledWemoScript = true;
                NSLog(@"Add");
             //   usecase1=true;
                
            }
            else if (([beacon.name isEqualToString:@"artw"]) && [Singleton instance].hasCalledWemoScript &&  (!artw1) && !(artw2))
            {  //if you go in either far zone or unknown region the script of turning the switch off will be executed
                
                NSLog(@"Erase");
                onOffString = @"off";
            //   params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"uuid" :beacon.uuid, @"major" :beacon.major, @"minor" :beacon.minor}; //
                params = @ {@"onoroff" : onOffString};
                params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"name":beacon.name};
                 NSLog(@"params are for artw %@",params);
             //   NSError *error =
                [wemoService runWemoScriptwithParams:params2];
              [Singleton instance].hasCalledWemoScript = false;
           //     usecase1=true;
                
                
            }
            }
//            {
//                WemoScriptService *wemoService2 = [WemoScriptService sharedClient];
//                wemoService2.mWemoScriptServiceDelegate = self;
//                
//                NSString *onOffString = @"";
//                NSDictionary *params = @ {};
//                NSDictionary *params2 = @ {};
//                NSString *deviceId=[[UIDevice currentDevice] model];
//            
//            if (([beacon.name isEqualToString:@"immw"] /*|| [beacon.name isEqualToString:@"immw"] || [beacon.name isEqualToString:@"gmlb"]|| [beacon.name isEqualToString:@"vizw"]|| [beacon.name isEqualToString:@"cmnw"] */) &&  ![Singleton instance].hasCalledWemoScript2   && (thisCLBeacon.proximity == CLProximityImmediate || thisCLBeacon.proximity==CLProximityNear ))//|| thisCLBeacon.proximity==CLProximityFar))
//            {
//                //if you are in immediate region the script of turning the switch on will be executed
//                onOffString = @"on";
//                params = @ {@"onoroff" : onOffString};
//                // params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"uuid" :beacon.uuid, @"major" :beacon.major, @"minor" :beacon.minor};//
//                params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"name":beacon.name};
//                NSLog(@"params are for immw %@",params);
//                // NSError *error =
//                [wemoService2 runWemoScriptwithParams:params2];//
//                     [Singleton instance].hasCalledWemoScript2 = true;
//              //  usecase2=true;
//                
//            }
//            else if (([beacon.name isEqualToString:@"immw"]/*|| [beacon.name isEqualToString:@"immw"] || [beacon.name isEqualToString:@"gmlb"]|| [beacon.name isEqualToString:@"vizw"]|| [beacon.name isEqualToString:@"cmnw"]*/) && [Singleton instance].hasCalledWemoScript2 && (thisCLBeacon.proximity == CLProximityFar || thisCLBeacon.proximity==CLProximityUnknown))//&& thisCLBeacon.proximity != CLProximityImmediate && thisCLBeacon.proximity != CLProximityNear )//&& thisCLBeacon.proximity != CLProximityFar)
//            {  //if you go in either far zone or unknown region the script of turning the switch off will be executed
//                onOffString = @"off";
//                //   params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"uuid" :beacon.uuid, @"major" :beacon.major, @"minor" :beacon.minor}; //
//                params = @ {@"onoroff" : onOffString};
//                params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"name":beacon.name};
//                NSLog(@"params are for immw %@",params);
//                //   NSError *error =
//                [wemoService2 runWemoScriptwithParams:params2];
//                  [Singleton instance].hasCalledWemoScript2 = false;
//              //  usecase2=true;
//                
//            }
//            }
//            {
//                WemoScriptService *wemoService3 = [WemoScriptService sharedClient];
//                wemoService3.mWemoScriptServiceDelegate = self;
//                
//                NSString *onOffString = @"";
//                NSDictionary *params = @ {};
//                NSDictionary *params2 = @ {};
//                NSString *deviceId=[[UIDevice currentDevice] model];
//                
//                if (([beacon.name isEqualToString:@"gmlb"] /*|| [beacon.name isEqualToString:@"immw"] || [beacon.name isEqualToString:@"gmlb"]|| [beacon.name isEqualToString:@"vizw"]|| [beacon.name isEqualToString:@"cmnw"] */) &&  ![Singleton instance].hasCalledWemoScript3   && (thisCLBeacon.proximity == CLProximityImmediate || thisCLBeacon.proximity==CLProximityNear))
//                {
//                    //if you are in immediate region the script of turning the switch on will be executed
//                    onOffString = @"on";
//                    params = @ {@"onoroff" : onOffString};
//                    // params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"uuid" :beacon.uuid, @"major" :beacon.major, @"minor" :beacon.minor};//
//                    params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"name":beacon.name};
//                    NSLog(@"params are for gmlb %@",params);
//                    // NSError *error =
//                    [wemoService3 runWemoScriptwithParams:params2];//
//                    [Singleton instance].hasCalledWemoScript3 = true;
//                    //  usecase2=true;
//                    
//                }
//                else if (([beacon.name isEqualToString:@"gmlb"]/*|| [beacon.name isEqualToString:@"immw"] || [beacon.name isEqualToString:@"gmlb"]|| [beacon.name isEqualToString:@"vizw"]|| [beacon.name isEqualToString:@"cmnw"]*/) && [Singleton instance].hasCalledWemoScript3 && thisCLBeacon.proximity != CLProximityImmediate && thisCLBeacon.proximity != CLProximityNear)
//                {  //if you go in either far zone or unknown region the script of turning the switch off will be executed
//                    onOffString = @"off";
//                    //   params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"uuid" :beacon.uuid, @"major" :beacon.major, @"minor" :beacon.minor}; //
//                    params = @ {@"onoroff" : onOffString};
//                    params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"name":beacon.name};
//                    NSLog(@"params are for gmlb %@",params);
//                    //   NSError *error =
//                    [wemoService3 runWemoScriptwithParams:params2];
//                    [Singleton instance].hasCalledWemoScript3 = false;
//                    //  usecase2=true;
//                    
//                }
//            }
//            {
//                WemoScriptService *wemoService4 = [WemoScriptService sharedClient];
//                wemoService4.mWemoScriptServiceDelegate = self;
//                
//                NSString *onOffString = @"";
//                NSDictionary *params = @ {};
//                NSDictionary *params2 = @ {};
//                NSString *deviceId=[[UIDevice currentDevice] model];
//                
//                if (([beacon.name isEqualToString:@"vizw"] /*|| [beacon.name isEqualToString:@"immw"] || [beacon.name isEqualToString:@"gmlb"]|| [beacon.name isEqualToString:@"vizw"]|| [beacon.name isEqualToString:@"cmnw"] */) &&  ![Singleton instance].hasCalledWemoScript4   && (thisCLBeacon.proximity == CLProximityImmediate || thisCLBeacon.proximity==CLProximityNear))
//                {
//                    //if you are in immediate region the script of turning the switch on will be executed
//                    onOffString = @"on";
//                    params = @ {@"onoroff" : onOffString};
//                    // params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"uuid" :beacon.uuid, @"major" :beacon.major, @"minor" :beacon.minor};//
//                    params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"name":beacon.name};
//                    NSLog(@"params are for vizw %@",params);
//                    // NSError *error =
//                    [wemoService4 runWemoScriptwithParams:params2];//
//                    [Singleton instance].hasCalledWemoScript4 = true;
//                    //  usecase2=true;
//                    
//                }
//                else if (([beacon.name isEqualToString:@"vizw"]/*|| [beacon.name isEqualToString:@"immw"] || [beacon.name isEqualToString:@"gmlb"]|| [beacon.name isEqualToString:@"vizw"]|| [beacon.name isEqualToString:@"cmnw"]*/) && [Singleton instance].hasCalledWemoScript4 && thisCLBeacon.proximity != CLProximityImmediate && thisCLBeacon.proximity != CLProximityNear)
//                {  //if you go in either far zone or unknown region the script of turning the switch off will be executed
//                    onOffString = @"off";
//                    //   params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"uuid" :beacon.uuid, @"major" :beacon.major, @"minor" :beacon.minor}; //
//                    params = @ {@"onoroff" : onOffString};
//                    params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"name":beacon.name};
//                    NSLog(@"params are for vizw %@",params);
//                    //   NSError *error =
//                    [wemoService4 runWemoScriptwithParams:params2];
//                    [Singleton instance].hasCalledWemoScript4 = false;
//                    //  usecase2=true;
//                    
//                }
//            }
//            {
//                WemoScriptService *wemoService5 = [WemoScriptService sharedClient];
//                wemoService5.mWemoScriptServiceDelegate = self;
//                
//                NSString *onOffString = @"";
//                NSDictionary *params = @ {};
//                NSDictionary *params2 = @ {};
//                NSString *deviceId=[[UIDevice currentDevice] model];
//                
//                if (([beacon.name isEqualToString:@"cmnw"] /*|| [beacon.name isEqualToString:@"immw"] || [beacon.name isEqualToString:@"gmlb"]|| [beacon.name isEqualToString:@"vizw"]|| [beacon.name isEqualToString:@"cmnw"] */) &&  ![Singleton instance].hasCalledWemoScript5   && (thisCLBeacon.proximity == CLProximityImmediate || thisCLBeacon.proximity==CLProximityNear))
//                {
//                    //if you are in immediate region the script of turning the switch on will be executed
//                    onOffString = @"on";
//                    params = @ {@"onoroff" : onOffString};
//                    // params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"uuid" :beacon.uuid, @"major" :beacon.major, @"minor" :beacon.minor};//
//                    params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"name":beacon.name};
//                    NSLog(@"params are for cmnw %@",params);
//                    // NSError *error =
//                    [wemoService5 runWemoScriptwithParams:params2];//
//                    [Singleton instance].hasCalledWemoScript5 = true;
//                    //  usecase2=true;
//                    
//                }
//                else if (([beacon.name isEqualToString:@"cmnw"]/*|| [beacon.name isEqualToString:@"immw"] || [beacon.name isEqualToString:@"gmlb"]|| [beacon.name isEqualToString:@"vizw"]|| [beacon.name isEqualToString:@"cmnw"]*/) && [Singleton instance].hasCalledWemoScript5 && thisCLBeacon.proximity != CLProximityImmediate && thisCLBeacon.proximity != CLProximityNear)
//                {  //if you go in either far zone or unknown region the script of turning the switch off will be executed
//                    onOffString = @"off";
//                    //   params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"uuid" :beacon.uuid, @"major" :beacon.major, @"minor" :beacon.minor}; //
//                    params = @ {@"onoroff" : onOffString};
//                    params2 = @ {@"onoroff" : onOffString, @"deviceId":deviceId, @"name":beacon.name};
//                    NSLog(@"params are for cmnw %@",params);
//                    //   NSError *error =
//                    [wemoService5 runWemoScriptwithParams:params2];
//                    [Singleton instance].hasCalledWemoScript5 = false;
//                    //  usecase2=true;
//                    
//                }
//            }
            
        }
        cell.textLabel.text = beaconName;
        
    }
    else if (indexPath.section == 1)
    {
        if (otherBeaconsArray.count > 0)
        {
            iBeacon *beacon = (iBeacon *)[otherBeaconsArray objectAtIndex:indexPath.row];
            beaconName = beacon.name;
            cell.detailTextLabel.text = [NSString stringWithFormat:@"Major: %@ | Minor: %@", beacon.major, beacon.minor];
        }
        cell.textLabel.text = beaconName;
    }   
    return cell;
    
}

-(void)wemoScriptRanSuccessfully
{
    //Currently unnecessary to do anything here
    //Maybe add a field in the geo table?
}

-(void)wemoScriptRanWithError:(NSError *)error
{
    NSLog(@"Error: %@", error);
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end

