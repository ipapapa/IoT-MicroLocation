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

@interface SecondViewController ()


@end

@implementation SecondViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    float height = self.view.bounds.size.height;
    float width = self.view.bounds.size.width;
    
    CGRect tableFrame = CGRectMake(0, 20, width,height);//568, 375);
    [Singleton instance].geoTableView = [[UITableView alloc]initWithFrame:tableFrame];
    [Singleton instance].geoTableView.allowsMultipleSelection = NO;
    [Singleton instance].geoTableView.delegate = self;
    [Singleton instance].geoTableView.dataSource = self;
    
    [self.view addSubview:[Singleton instance].geoTableView];
}

- (NSMutableArray *)createGeoBeaconsArray
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

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    NSString *title = @"";
    if (section == 0)
        title = @"Geofencing Beacons";
    else if (section == 1)
        title = @"Other Beacons";
        
    return title;
}

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
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
        numRows = [Singleton instance].geoBeacons.count;
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
            
            if ([beacon.name isEqualToString:@"TV"])
            {
                //Call Raspberry Pi Script
            }
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


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end

