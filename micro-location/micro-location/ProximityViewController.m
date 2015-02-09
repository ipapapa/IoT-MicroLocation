//
//  ProximityViewController.m
//  micro-location
//
//  Created by Ian Hamilton on 2/9/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import "ProximityViewController.h"
#import <CoreLocation/CoreLocation.h>
#import "Singleton.h"

@interface ProximityViewController ()

@end

@implementation ProximityViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // Override point for customization after application launch.
    
    //CGRect tableFrame =  CGRectMake(self.view.bounds.origin.x, self.view.bounds.origin.y -50, self.view.bounds.size.width, self.view.bounds.size.height);
    
    CGRect tableFrame = CGRectMake(0, 100, 400,400);//568, 375);
    [Singleton instance].tableView = [[UITableView alloc]initWithFrame:tableFrame];
    
    [Singleton instance].tableView.delegate = self;
    [Singleton instance].tableView.dataSource = self;
    
    [self.view addSubview:[Singleton instance].tableView];
    
}



- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [Singleton instance].beacons.count;
    
    
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
    
    CLBeacon *beacon = (CLBeacon*)[[Singleton instance].beacons objectAtIndex:indexPath.row];
    NSString *proximityLabel = @"";
    switch (beacon.proximity) {
        case CLProximityFar:
            proximityLabel = @"Far";
            break;
        case CLProximityNear:
            proximityLabel = @"Near";
            break;
        case CLProximityImmediate:
            proximityLabel = @"Immediate";
            break;
        case CLProximityUnknown:
            proximityLabel = @"Unknown";
            break;
    }
    
    cell.textLabel.text = proximityLabel;
    
    int e=(int)beacon.rssi;
    double d= (e+55.0)/(-26.4432);
    double result=pow(10,d);
    NSString *beaconName;
    if(beacon.major.intValue==0)
    {
        beaconName=@"Beacon 1";
    }
    else if(beacon.major.intValue==2)
    {
        beaconName=@"Beacon 2";
        
    }
    else if(beacon.major.intValue==3)
    {
        beaconName=@"Beacon 3";
    }
    NSString *detailLabel = [NSString stringWithFormat:@"%@,RSSI: %d, Dist(m): %f"
                             ,beaconName,(int)beacon.rssi,result ];
    cell.detailTextLabel.font = [UIFont fontWithName:@"Helvetica" size:10.0];
    cell.detailTextLabel.lineBreakMode = NSLineBreakByWordWrapping; // Pre-iOS6 use UILineBreakModeWordWrap
    cell.detailTextLabel.numberOfLines = 3;  // 0 means no max.
    cell.detailTextLabel.text = detailLabel;
    return cell;
    
}
@end
