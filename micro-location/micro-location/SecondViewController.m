//
//  SecondViewController.m
//  micro-location
//
//  Created by Ian Hamilton on 2/9/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import "SecondViewController.h"
#import "Singleton.h"
#import <CoreLocation/CoreLocation.h>

@interface SecondViewController ()

@property (nonatomic, strong) NSMutableArray *imageViews;

@end

@implementation SecondViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    self.imageViews = [NSMutableArray array];
    
    NSString *notificationName = @"UpdateBeaconsNotification";
    
    [[NSNotificationCenter defaultCenter]
     addObserver:self
     selector:@selector(updateBeacons)
     name:notificationName
     object:nil];
}

- (void)viewDidAppear:(BOOL)animated
{
    self.imageViews = [NSMutableArray arrayWithArray:@[]];
    
    for (CLBeacon *beacon in [Singleton instance].beacons)
    {
        int index = (int)[[Singleton instance].beacons indexOfObject:beacon];
        index += 1;
        
        
        UIImage *orig_beaconImage = [UIImage imageNamed:@"beacon.png"];
        
        // scaling set to 2.0 makes the image 1/2 the size.
        self.beaconImage =
        [UIImage imageWithCGImage:[orig_beaconImage CGImage]
                            scale:(orig_beaconImage.scale * 2.0)
                      orientation:(orig_beaconImage.imageOrientation)];
        
        CGRect immediateImageFrame = CGRectMake(150, 150 + index*40, 30, 30);
        
        UIImageView *immediateImageView = [[UIImageView alloc]initWithFrame:immediateImageFrame];
        immediateImageView.image = self.beaconImage;
        
        CGRect nearImageFrame = CGRectMake(110, (210 + index *40), 30, 30);
        
        UIImageView *nearImageView = [[UIImageView alloc]initWithFrame:nearImageFrame];
        nearImageView.image = self.beaconImage;
        
        CGRect farImageFrame = CGRectMake(25, 300, 30, 30);
        
        UIImageView *farImageView = [[UIImageView alloc]initWithFrame:farImageFrame];
        farImageView.image = self.beaconImage;
        
        
        
        switch (beacon.proximity)
        {
            case CLProximityFar:
                //proximityLabel = @"Far";
                [self.view addSubview:farImageView];
                [self.imageViews addObject:farImageView];
                
                break;
                
            case CLProximityNear:
                //proximityLabel = @"Near";
                [self.view addSubview:nearImageView];
                [self.imageViews addObject:nearImageView];
                
                break;
            case CLProximityImmediate:
                [self.view addSubview:immediateImageView];
                [self.imageViews addObject:immediateImageView];
                //proximityLabel = @"Immediate";
                break;
            case CLProximityUnknown:
                //proximityLabel = @"Unknown";
                break;
        }
    }
}

- (void)updateBeacons
{
    for (UIImageView *imageView in self.imageViews)
    {
        [imageView performSelectorOnMainThread:@selector(removeFromSuperview) withObject:nil waitUntilDone:NO];
    }
    
    self.imageViews = [NSMutableArray arrayWithArray:@[]];
    
    for (CLBeacon *beacon in [Singleton instance].beacons)
    {
        int index = (int)[[Singleton instance].beacons indexOfObject:beacon];
        index += 1;
        
        
        UIImage *orig_beaconImage = [UIImage imageNamed:@"beacon.png"];
        
        // scaling set to 2.0 makes the image 1/2 the size.
        self.beaconImage =
        [UIImage imageWithCGImage:[orig_beaconImage CGImage]
                            scale:(orig_beaconImage.scale * 2.0)
                      orientation:(orig_beaconImage.imageOrientation)];
        
        CGRect immediateImageFrame = CGRectMake(150, 150 + index*40, 30, 30);
        
        UIImageView *immediateImageView = [[UIImageView alloc]initWithFrame:immediateImageFrame];
        immediateImageView.image = self.beaconImage;
        
        CGRect nearImageFrame = CGRectMake(110, (210 + index *40), 30, 30);
        
        UIImageView *nearImageView = [[UIImageView alloc]initWithFrame:nearImageFrame];
        nearImageView.image = self.beaconImage;
        
        CGRect farImageFrame = CGRectMake(25, 300, 30, 30);
        
        UIImageView *farImageView = [[UIImageView alloc]initWithFrame:farImageFrame];
        farImageView.image = self.beaconImage;
        
        
        
        switch (beacon.proximity)
        {
            case CLProximityFar:
                //proximityLabel = @"Far";
                [self.view addSubview:farImageView];
                [self.imageViews addObject:farImageView];
                
                break;
                
            case CLProximityNear:
                //proximityLabel = @"Near";
                [self.view addSubview:nearImageView];
                [self.imageViews addObject:nearImageView];
                
                break;
            case CLProximityImmediate:
                [self.view addSubview:immediateImageView];
                [self.imageViews addObject:immediateImageView];
                //proximityLabel = @"Immediate";
                break;
            case CLProximityUnknown:
                //proximityLabel = @"Unknown";
                break;
        }
    }
}

- (void)viewDidDisappear:(BOOL)animated
{
    for (UIImageView *imageView in self.imageViews)
    {
        [imageView performSelectorOnMainThread:@selector(removeFromSuperview) withObject:nil waitUntilDone:NO];
    }
    
}



- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end

