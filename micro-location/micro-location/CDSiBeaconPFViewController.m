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
#define PXPERMETER (320.0/6.0)
#define ARENAWIDTH 6.0//5.851 //4.0//7.0
#define ARENAHEIGHT 6.0//10.24//7.0

// Define landmark locations and identification
#if USELANDMARKSETONE

//#define PROXIMITY_UUID
#define LANDMARKSMAJOR 1
#define LANDMARK1COLOR [UIColor greenColor]
#define LANDMARK1MINOR 1
#define LANDMARK1X 0.4
#define LANDMARK1Y 0.2
#define LANDMARK2COLOR [UIColor purpleColor]
#define LANDMARK2MINOR 2
#define LANDMARK2X 3.6
#define LANDMARK2Y 0.2
#define LANDMARK3COLOR [UIColor blueColor]
#define LANDMARK3MINOR 3
#define LANDMARK3X 2.0
#define LANDMARK3Y 3.0

#endif

// Filter parameters
#define PARTICLECOUNT 1000//500
#define MEASUREMENTINTERVAL 0.0005//0.5
#define MEASUREMENTSIGMA 0.4//2.0
#define NOISESIGMA 0.1//0.1

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
//The title for the table
/*- (NSString*)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return [NSString stringWithFormat:@"Landmarks [%@]", self.beaconMgr.class];
}*/


- (UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"BeaconCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    CDSBeaconLandmark* landmark = self.landmarks[indexPath.row];
    
    // Configure the cell...
    cell.textLabel.text = [NSString stringWithFormat:@"%@ (%lddB)", landmark.ident, (long)landmark.rssi];
    cell.textLabel.textColor = landmark.color;
    
    cell.detailTextLabel.text = [NSString stringWithFormat:@"range: %4.2fm  mean: %4.2fdB  std. dev: ±%4.2f",
                                 landmark.meters, landmark.meanRssi, landmark.stdDeviationRssi];
    
    
    //
    //P1,P2,P3 is the point and 2-dimension vector
    NSMutableArray *P1 = [[NSMutableArray alloc] initWithCapacity:0];
    [P1 addObject:[NSNumber numberWithDouble:LANDMARK1X]];
    [P1 addObject:[NSNumber numberWithDouble:LANDMARK1Y]];
    
    
    NSMutableArray *P2 = [[NSMutableArray alloc] initWithCapacity:0];
    [P2 addObject:[NSNumber numberWithDouble:LANDMARK2X]];
    [P2 addObject:[NSNumber numberWithDouble:LANDMARK2Y]];
    
    NSMutableArray *P3 = [[NSMutableArray alloc] initWithCapacity:0];
    [P3 addObject:[NSNumber numberWithDouble:LANDMARK3X]];
    [P3 addObject:[NSNumber numberWithDouble:LANDMARK3Y]];
    
    //this is the distance between all the points and the unknown point
    //if(landmark.ident)
    double DistA;
    double DistB;
    double DistC;
    
    CDSBeaconLandmark *lmark1 = self.landmarks[0];
    DistA = lmark1.meters;
    
    CDSBeaconLandmark *lmark2 = self.landmarks[1];
    DistB = lmark2.meters;
    
    CDSBeaconLandmark *lmark3 = self.landmarks[2];
    DistC = lmark3.meters;
    
    // ex = (P2 - P1)/(numpy.linalg.norm(P2 - P1))
    NSMutableArray *ex = [[NSMutableArray alloc] initWithCapacity:0];
    double temp = 0;
    for (int i = 0; i < [P1 count]; i++) {
        double t1 = [[P2 objectAtIndex:i] doubleValue];
        double t2 = [[P1 objectAtIndex:i] doubleValue];
        double t = t1 - t2;
        temp += (t*t);
    }
    for (int i = 0; i < [P1 count]; i++) {
        double t1 = [[P2 objectAtIndex:i] doubleValue];
        double t2 = [[P1 objectAtIndex:i] doubleValue];
        double exx = (t1 - t2)/sqrt(temp);
        [ex addObject:[NSNumber numberWithDouble:exx]];
    }
    
    // i = dot(ex, P3 - P1)
    NSMutableArray *p3p1 = [[NSMutableArray alloc] initWithCapacity:0];
    for (int i = 0; i < [P3 count]; i++) {
        double t1 = [[P3 objectAtIndex:i] doubleValue];
        double t2 = [[P1 objectAtIndex:i] doubleValue];
        double t3 = t1 - t2;
        [p3p1 addObject:[NSNumber numberWithDouble:t3]];
    }
    
    double ival = 0;
    for (int i = 0; i < [ex count]; i++) {
        double t1 = [[ex objectAtIndex:i] doubleValue];
        double t2 = [[p3p1 objectAtIndex:i] doubleValue];
        ival += (t1*t2);
    }
    
    // ey = (P3 - P1 - i*ex)/(numpy.linalg.norm(P3 - P1 - i*ex))
    NSMutableArray *ey = [[NSMutableArray alloc] initWithCapacity:0];
    double p3p1i = 0;
    for (int  i = 0; i < [P3 count]; i++) {
        double t1 = [[P3 objectAtIndex:i] doubleValue];
        double t2 = [[P1 objectAtIndex:i] doubleValue];
        double t3 = [[ex objectAtIndex:i] doubleValue] * ival;
        double t = t1 - t2 -t3;
        p3p1i += (t*t);
    }
    for (int i = 0; i < [P3 count]; i++) {
        double t1 = [[P3 objectAtIndex:i] doubleValue];
        double t2 = [[P1 objectAtIndex:i] doubleValue];
        double t3 = [[ex objectAtIndex:i] doubleValue] * ival;
        double eyy = (t1 - t2 - t3)/sqrt(p3p1i);
        [ey addObject:[NSNumber numberWithDouble:eyy]];
    }
    
    
    // ez = numpy.cross(ex,ey)
    // if 2-dimensional vector then ez = 0
    NSMutableArray *ez = [[NSMutableArray alloc] initWithCapacity:0];
    double ezx;
    double ezy;
    double ezz;
    if ([P1 count] !=3){ ezx = 0;
        ezy = 0;
        ezz = 0;
        
    }
    else{
        ezx = ([[ex objectAtIndex:1] doubleValue]*[[ey objectAtIndex:2]doubleValue]) - ([[ex objectAtIndex:2]doubleValue]*[[ey objectAtIndex:1]doubleValue]);
        ezy = ([[ex objectAtIndex:2] doubleValue]*[[ey objectAtIndex:0]doubleValue]) - ([[ex objectAtIndex:0]doubleValue]*[[ey objectAtIndex:2]doubleValue]);
        ezz = ([[ex objectAtIndex:0] doubleValue]*[[ey objectAtIndex:1]doubleValue]) - ([[ex objectAtIndex:1]doubleValue]*[[ey objectAtIndex:0]doubleValue]);
        
    }
    
    [ez addObject:[NSNumber numberWithDouble:ezx]];
    [ez addObject:[NSNumber numberWithDouble:ezy]];
    [ez addObject:[NSNumber numberWithDouble:ezz]];
    
    
    // d = numpy.linalg.norm(P2 - P1)
    double d = sqrt(temp);
    
    // j = dot(ey, P3 - P1)
    double jval = 0;
    for (int i = 0; i < [ey count]; i++) {
        double t1 = [[ey objectAtIndex:i] doubleValue];
        double t2 = [[p3p1 objectAtIndex:i] doubleValue];
        jval += (t1*t2);
    }
    
    // x = (pow(DistA,2) - pow(DistB,2) + pow(d,2))/(2*d)
    double xval = (pow(DistA,2) - pow(DistB,2) + pow(d,2))/(2*d);
    
    // y = ((pow(DistA,2) - pow(DistC,2) + pow(i,2) + pow(j,2))/(2*j)) - ((i/j)*x)
    double yval = ((pow(DistA,2) - pow(DistC,2) + pow(ival,2) + pow(jval,2))/(2*jval)) - ((ival/jval)*xval);
    
    // z = sqrt(pow(DistA,2) - pow(x,2) - pow(y,2))
    // if 2-dimensional vector then z = 0
    double zval;
    if ([P1 count] !=3){
        zval = 0;
    }else{
        zval = sqrt(pow(DistA,2) - pow(xval,2) - pow(yval,2));
    }
    
    // triPt = P1 + x*ex + y*ey + z*ez
    NSMutableArray *triPt = [[NSMutableArray alloc] initWithCapacity:0];
    for (int i = 0; i < [P1 count]; i++) {
        double t1 = [[P1 objectAtIndex:i] doubleValue];
        double t2 = [[ex objectAtIndex:i] doubleValue] * xval;
        double t3 = [[ey objectAtIndex:i] doubleValue] * yval;
        double t4 = [[ez objectAtIndex:i] doubleValue] * zval;
        double triptx = t1+t2+t3+t4;
        [triPt addObject:[NSNumber numberWithDouble:triptx]];
    }
    
    //    NSLog(@"ex %@",ex);
    //    NSLog(@"i %f",ival);
    //    NSLog(@"ey %@",ey);
    //    NSLog(@"d %f",d);
    //    NSLog(@"j %f",jval);
    //    NSLog(@"x %f",xval);
    //    NSLog(@"y %f",yval);
    //    NSLog(@"y %f",yval);
    NSLog(@"final result %@",triPt);
    
    
    
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
