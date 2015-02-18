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

@property (nonatomic, strong) NSMutableArray *imageViews;
@property (nonatomic, strong) NSIndexPath *checkedPath;
@property (nonatomic, strong) CLBeacon *checkedBeacon;
@property int checkedBeaconMinorValue;

@property int immediateIndex;
@property int nearIndex;
@property int farIndex;


@end

@implementation ProximityViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // Override point for customization after application launch.
    
    //CGRect tableFrame =  CGRectMake(self.view.bounds.origin.x, self.view.bounds.origin.y -50, self.view.bounds.size.width, self.view.bounds.size.height);
    
    
    UIImage *orig_beaconImage = [UIImage imageNamed:@"beacon.png"];
    
    // scaling set to 2.0 makes the image 1/2 the size.
    self.beaconImage =
    [UIImage imageWithCGImage:[orig_beaconImage CGImage]
                        scale:(orig_beaconImage.scale * 2.0)
                  orientation:(orig_beaconImage.imageOrientation)];
    
    
    UIImage *orig_sel_beaconImage = [UIImage imageNamed:@"beacon_selected.png"];
    self.sel_beaconImage =
    [UIImage imageWithCGImage:[orig_sel_beaconImage CGImage]
                        scale:(orig_sel_beaconImage.scale * 2.0)
                  orientation:(orig_sel_beaconImage.imageOrientation)];
    
    self.imageViews = [NSMutableArray array];
    
    //default to -1 so it doesn't match with any table rows
    self.checkedPath = [NSIndexPath indexPathForRow:-1 inSection:0];
    
    float height = self.view.bounds.size.height  - 445;
    float width = self.view.bounds.size.width;
    
    CGRect tableFrame = CGRectMake(0, 395, width,height);//568, 375);
    [Singleton instance].tableView = [[UITableView alloc]initWithFrame:tableFrame];
    [Singleton instance].tableView.allowsMultipleSelection = NO;
    [Singleton instance].tableView.delegate = self;
    [Singleton instance].tableView.dataSource = self;
    
    [self.view addSubview:[Singleton instance].tableView];
    
}

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [Singleton instance].beacons.count;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    //Uncheck the previous checked row
    if (self.checkedBeacon)
    {
        UITableViewCell *uncheckCell = [tableView cellForRowAtIndexPath:self.checkedPath];
        uncheckCell.accessoryType = UITableViewCellAccessoryNone;
        self.checkedBeacon = nil;
    }
    

    CLBeacon *beacon = [[Singleton instance].beacons objectAtIndex:indexPath.row];
    self.checkedBeacon = beacon;

    
    if([self.checkedPath isEqual:indexPath])
    {
        self.checkedPath = nil;
        self.checkedBeacon = nil;
    }
    else
    {
        UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
        self.checkedPath = indexPath;
    }
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
    
    CLBeacon *beacon = (CLBeacon *)[[Singleton instance].beacons objectAtIndex:indexPath.row];
    
    NSString *proximityLabel = @"";
    
    if(self.checkedBeacon.minor == beacon.minor)
    {
        
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
        self.checkedPath = indexPath;
    }
    else
    {
        cell.accessoryType = UITableViewCellAccessoryNone;
    }

    
    //new reload of all cells
    if (indexPath.row == 0)
    {
        self.immediateIndex = 0;
        self.nearIndex = 0;
        self.farIndex = 0;
        
        for (UIImageView *imageView in self.imageViews)
        {
            [imageView performSelectorOnMainThread:@selector(removeFromSuperview) withObject:nil waitUntilDone:YES];
        }
        self.imageViews = [NSMutableArray arrayWithArray:@[]];
    }
    
    CGRect immediateImageFrame;
    
    if (self.immediateIndex <= 2)
    {
        immediateImageFrame = CGRectMake(155 + (self.immediateIndex * 15), 180, 15, 15);
    }
    
    //set index and proximity label
    switch (beacon.proximity)
    {
        case CLProximityFar:
            proximityLabel = @"Far";
            self.farIndex++;
            break;
        case CLProximityNear:
            proximityLabel = @"Near";
            self.nearIndex++;
            break;
        case CLProximityImmediate:
            proximityLabel = @"Immediate";
            self.immediateIndex++;
            break;
        case CLProximityUnknown:
            proximityLabel = @"Unknown";
            break;
            
    }
    
    
    if (self.immediateIndex > 2 && self.immediateIndex < 5)
    {
        
        immediateImageFrame = CGRectMake(155 + ((self.immediateIndex -3) * 15), 210, 15, 15);
    }
    
    //TODO: Need to adapt more to circular frame for n number of beacons
    UIImageView *immediateImageView = [[UIImageView alloc]initWithFrame:immediateImageFrame];
    if (cell.accessoryType == UITableViewCellAccessoryCheckmark)
    {
        immediateImageView.image = self.sel_beaconImage;
    }
    else
    {
        immediateImageView.image = self.beaconImage;
    }
    
    
    //TODO: Need to adapt to circular frame for n number of beacons
    CGRect nearImageFrame = CGRectMake(135 + (self.nearIndex * 15), 130 + (self.nearIndex * 15), 15, 15);
    
    UIImageView *nearImageView = [[UIImageView alloc]initWithFrame:nearImageFrame];
    
    if (cell.accessoryType == UITableViewCellAccessoryCheckmark)
    {
        nearImageView.image = self.sel_beaconImage;
    }
    else
    {
        nearImageView.image = self.beaconImage;
    }
    
    //TODO: Need to adapt to circular frame for n number of beacons
    CGRect farImageFrame = CGRectMake(60 + (self.farIndex * 15), 110 + (self.farIndex * 15) , 15, 15);
    
    UIImageView *farImageView = [[UIImageView alloc]initWithFrame:farImageFrame];
    if (cell.accessoryType == UITableViewCellAccessoryCheckmark)
    {
        farImageView.image = self.sel_beaconImage;
    }
    else
    {
        farImageView.image = self.beaconImage;
    }
    
    //load images on screen
    switch (beacon.proximity)
    {
        case CLProximityFar:
            [self.view addSubview:farImageView];
            [self.imageViews addObject:farImageView];
            break;
        case CLProximityNear:
            [self.view addSubview:nearImageView];
            [self.imageViews addObject:nearImageView];
            break;
        case CLProximityImmediate:
            [self.view addSubview:immediateImageView];
            [self.imageViews addObject:immediateImageView];
            break;
        case CLProximityUnknown:
            proximityLabel = @"Unknown";
            break;
            
    }
    
    
    cell.textLabel.text = proximityLabel;
    
    int e=(int)beacon.rssi;
    double d= (e+55.0)/(-26.4432);
    double result=pow(10,d);
    
    NSString *beaconName = @"Placeholder";
    
    if(beacon.major.intValue==19712)
    {
        beaconName=@"Mint Green";
    }
    else if(beacon.major.intValue==28989)
    {
        beaconName=@"Ice Blue";
    }
    else if(beacon.major.intValue==55115)
    {
        beaconName=@"Blue Berry";
    }
    
    NSString *detailLabel = [NSString stringWithFormat:@"%@, RSSI: %d, Dist(m): %f",
                                                    beaconName, (int)beacon.rssi, result ];
    
    cell.detailTextLabel.font = [UIFont fontWithName:@"Helvetica" size:10.0];
    cell.detailTextLabel.lineBreakMode = NSLineBreakByWordWrapping; // Pre-iOS6 use UILineBreakModeWordWrap
    cell.detailTextLabel.numberOfLines = 3;  // 0 means no max.
    cell.detailTextLabel.text = detailLabel;
    
    return cell;
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
@end
