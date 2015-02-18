//
//  CDSiBeaconPFViewController.m
//  micro-location
//
//  Created by Fahim Zafari on 2/18/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#pragma mark - Adjustable parameters


#define USEGENERICSDK 1
//#define USEBLEONLYAPI 1 // Not yet supported

// Select one set of landmarks
#define USELANDMARKSETONE 1
//#define USELANDMARKSETTWO 1

//static NSString* beaconRegionId = @"com.codeswell.ibeacondemo";

#define PROXIMITY_UUID [[NSUUID alloc] initWithUUIDString:@"F4913F46-75F4-9134-913F-4913F4913F49"]
// Define the arena (Measurements in meters)
#define PXPERMETER (320.0/7.0)
#define ARENAWIDTH 7.0
#define ARENAHEIGHT 5.0

// Define landmark locations and identification
#if USELANDMARKSETONE

//#define PROXIMITY_UUID
#define LANDMARKSMAJOR 1
#define LANDMARK1COLOR [UIColor greenColor]
#define LANDMARK1MINOR 1
#define LANDMARK1X 1.0
#define LANDMARK1Y 1.0
#define LANDMARK2COLOR [UIColor purpleColor]
#define LANDMARK2MINOR 2
#define LANDMARK2X 3.3
#define LANDMARK2Y 1.0
#define LANDMARK3COLOR [UIColor blueColor]
#define LANDMARK3MINOR 3
#define LANDMARK3X 2.2
#define LANDMARK3Y 4.6

#endif

// Filter parameters
#define PARTICLECOUNT 200 //500
#define MEASUREMENTINTERVAL 0.5
#define MEASUREMENTSIGMA 2.0
#define NOISESIGMA 0.1

#pragma mark -


#import "CDSiBeaconPFViewController.h"
#import "CDSXYParticleFilter.h"
#import "CDSArenaView.h"
#import "BeaconManagerProtocol.h"
#import "GenericBeaconManager.h"


@interface CDSiBeaconPFViewController () <CDSXYParticleFilterDelegate, UITableViewDataSource, UITableViewDelegate, BeaconManagerDelegate>

@property (weak, nonatomic) IBOutlet UITableView *beaconTable;
@property (weak, nonatomic) IBOutlet CDSArenaView *arenaView;

@property (nonatomic, strong) CDSXYParticleFilter* particleFilter;
@property (nonatomic, strong) NSArray* landmarks;
@property (nonatomic, strong) NSDictionary* landmarkHash;
@property (nonatomic, strong) NSTimer* timer;

@property (nonatomic, strong) id<BeaconManager> beaconMgr;

@end


@implementation CDSiBeaconPFViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.arenaView.pxPerMeter = PXPERMETER;
    
    static NSString* beaconRegionId = @"com.codeswell.ibeacondemo";

    self.beaconMgr = [[GenericBeaconManager alloc] initWithProximityUuid:PROXIMITY_UUID
                                                                regionId:beaconRegionId];
    //static NSString* beaconRegionId = @"com.codeswell.ibeacondemo";

    
    self.beaconMgr.delegate = self;
    [self.beaconMgr beginRanging];
}

- (void)viewWillAppear:(BOOL)animated {
    
    if (self.particleFilter == nil) {
        self.particleFilter = [[CDSXYParticleFilter alloc] initWithParticleCount:PARTICLECOUNT
                                                                            minX:0.0
                                                                            maxX:ARENAWIDTH
                                                                            minY:0.0
                                                                            maxY:ARENAHEIGHT
                                                                      noiseSigma:NOISESIGMA];
        self.particleFilter.delegate = self;
    }
    
    if (self.landmarks == nil) {
        self.landmarks = @[
                           [CDSBeaconLandmark landmarkWithIdent:[CDSBeaconLandmark identFromMajor:LANDMARKSMAJOR minor:LANDMARK1MINOR]
                                                              x:LANDMARK1X
                                                              y:LANDMARK1Y
                                                          color:LANDMARK1COLOR],
                           [CDSBeaconLandmark landmarkWithIdent:[CDSBeaconLandmark identFromMajor:LANDMARKSMAJOR minor:LANDMARK2MINOR]
                                                              x:LANDMARK2X
                                                              y:LANDMARK2Y
                                                          color:LANDMARK2COLOR],
                           [CDSBeaconLandmark landmarkWithIdent:[CDSBeaconLandmark identFromMajor:LANDMARKSMAJOR minor:LANDMARK3MINOR]
                                                              x:LANDMARK3X
                                                              y:LANDMARK3Y
                                                          color:LANDMARK3COLOR]
                           ];
        self.arenaView.landmarks = self.landmarks;
        
        
        
        NSMutableDictionary* tmpLandmarkHash = [NSMutableDictionary dictionary];
        for (CDSBeaconLandmark* landmark in self.landmarks) {
            tmpLandmarkHash[landmark.ident] = landmark;
        }
        self.landmarkHash = tmpLandmarkHash;
    }
    
    // Used for timer-driven measurement/sampling
    //    if (self.timer == nil) {
    //        self.timer = [NSTimer scheduledTimerWithTimeInterval:MEASUREMENTINTERVAL
    //                                                      target:self
    //                                                    selector:@selector(advanceParticleFilter)
    //                                                    userInfo:nil
    //                                                     repeats:YES];
    //    }
    
}

// Used for timer-driven measurement/sampling
//- (void)viewWillDisappear:(BOOL)animated {
//    [self.timer invalidate];
//    self.timer = nil;
//}
//
//- (void)advanceParticleFilter {
//    // [self.particleFilter applyMotion];
//    [self.particleFilter applyMeasurements:self.landmarks];
//}


#pragma mark CDSXYParticleFilterDelegate protocol

- (void)xyParticleFilter:(CDSXYParticleFilter*)filter moveParticle:(CDSXYParticle*)particle {
    CDSXYParticle* p = particle;
    
    // Calculate incremental movement (There's none in our case)
    double delta_x = 0.0;
    double delta_y = 0.0;
    
    // Apply movement
    p.x += delta_x;
    p.y += delta_y;
}


- (double)xyParticleFilter:(CDSXYParticleFilter*)filter getLikelihood:(CDSXYParticle*)particle withMeasurements:(id)measurements {
    CDSXYParticle* p = particle;
    
    NSArray* measuredLandmarks = measurements;
    double confidence = 1.0;
    
    for (CDSBeaconLandmark* landmark in measuredLandmarks) {
        
        if (landmark.rssi < -10) {
            
            // Get the expected distance in pixels, using Euclidian distance (a^2 + b^2 = c^2)
            double expectedDistance = sqrtf(
                                            powf((landmark.x-p.x), 2)
                                            + powf((landmark.y-p.y), 2)
                                            );
            
            double error = landmark.meters - expectedDistance;
            confidence *= [self confidenceFromDisplacement:error sigma:MEASUREMENTSIGMA];
        }
    }
    
    return confidence;
}


// Optional
//- (double)xyParticleFilter:(CDSXYParticleFilter*)filter noiseWithMean:(double)m sigma:(double)s;


- (void)xyParticleFilter:(CDSXYParticleFilter*)filter particlesNormalized:(NSArray*)particles {
    self.arenaView.particles = particles;
    [self.arenaView setNeedsDisplay];
}


// Optional
//- (void)xyParticleFilter:(CDSXYParticleFilter*)filter particlesResampled:(NSArray*)particles;


#pragma mark ESTBeaconManagerDelegate protocol

- (void)rangedBeacons:(NSArray *)beacons {
    
    // Clear the rssi on all landmarks, so only ones we've ranged will have rssi values
    [self.landmarks enumerateObjectsUsingBlock:^(CDSBeaconLandmark* landmark, NSUInteger idx, BOOL *stop) {
        landmark.rssi = 0;
    }];
    
    for (id beacon in beacons) {
        NSString* ident = [CDSBeaconLandmark identFromMajor:[[beacon valueForKey:@"major"] integerValue]
                                                      minor:[[beacon valueForKey:@"minor"] integerValue]];
        CDSBeaconLandmark* landmark = self.landmarkHash[ident];
        
        // Get an RSSI, if we can
        NSNumber* rssi = [beacon valueForKey:@"rssi"];
        
        // Set it on the landmark, if we got one
        if (rssi != nil) {
            landmark.rssi = [rssi integerValue];
        }
    }
    
    // Show the data and iterate the filter
    [self.beaconTable reloadData];
    [self.particleFilter applyMeasurements:self.landmarks];
}


#pragma mark UITableViewDataSource Protocol

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.landmarks.count;
}


#pragma mark UITableViewDelegate Protocol

- (NSString*)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return [NSString stringWithFormat:@"Landmarks [%@]", self.beaconMgr.class];
}


- (UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"BeaconCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    CDSBeaconLandmark* landmark = self.landmarks[indexPath.row];
    
    // Configure the cell...
    cell.textLabel.text = [NSString stringWithFormat:@"%@ (%lddB)", landmark.ident, (long)landmark.rssi];
    cell.textLabel.textColor = landmark.color;
    
    cell.detailTextLabel.text = [NSString stringWithFormat:@"range: %4.2fm  mean: %4.2fdB  std. dev: ±%4.2f",
                                 landmark.meters, landmark.meanRssi, landmark.stdDeviationRssi];
    
    return cell;
}


#pragma mark Helpers

- (double)confidenceFromDisplacement:(double)displacement sigma:(double)sigma {
    
    // Based on http://stackoverflow.com/a/656992/46731
    // No need to normalize it, since the particle filter will normalize all weights
    
    double ret = exp( -pow(displacement, 2) / (2 * pow(sigma, 2)) );
    return ret;
}



@end
