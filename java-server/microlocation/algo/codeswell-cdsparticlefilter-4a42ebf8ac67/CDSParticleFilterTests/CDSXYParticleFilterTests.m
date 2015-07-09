//
//  CDSXYParticleFilterTests.m
//  CDSParticleFilter
//
//  Created by Andrew Craze on 2/19/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "CDSXYParticleFilter.h"

@interface CDSXYParticleFilterTests : XCTestCase <CDSXYParticleFilterDelegate>

@property (nonatomic, strong) CDSXYParticleFilter* filter;
@property (nonatomic, assign) BOOL failed;

@end


@implementation CDSXYParticleFilterTests

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

static NSUInteger particleCount = 100;
static double range = 100.0;


- (void)testParticleCount {
    self.filter = [[CDSXYParticleFilter alloc] initWithParticleCount:particleCount
                                                                minX:0
                                                                maxX:range
                                                                minY:0
                                                                maxY:range
                                                          noiseSigma:0.1];
    
    self.filter.delegate = self;
    
    [self.filter applyMeasurements:nil];
    XCTAssert(!self.failed, @"Standard deviation after 1 iteration very low -- extremely statistically unlikely");
    
    [self.filter applyMeasurements:nil];
    XCTAssert(!self.failed, @"Standard deviation after 2 iterations very low -- extremely statistically unlikely");
    
}


#pragma mark CDSXYParticleFilterDelegate protocol

- (void)xyParticleFilter:(CDSXYParticleFilter*)filter moveParticle:(CDSXYParticle*)particle {
    // No movement
    return;
}


- (double)xyParticleFilter:(CDSXYParticleFilter*)filter getLikelihood:(CDSXYParticle*)particle withMeasurements:(id)measurements {
    // Equal likelihood for all
    double likelihood =  1.0 / (double)particleCount;
    return likelihood;
}


//- (double)xyParticleFilter:(CDSXYParticleFilter*)filter noiseWithMean:(double)m sigma:(double)s;


- (void)xyParticleFilter:(CDSXYParticleFilter*)filter particlesResampled:(NSArray*)particles {
    NSLog(@"X: Mean=%f  Std Dev=%f", [self meanX:particles], [self standardDeviationX:particles]);
    NSLog(@"Y: Mean=%f  Std Dev=%f", [self meanY:particles], [self standardDeviationY:particles]);
    if ([self standardDeviationX:particles] < 500 || [self standardDeviationY:particles] < 500) {
        self.failed = YES;
    }
}


#pragma mark Helpers

- (double)meanX:(NSArray*)particles {
    double total = 0;
    for (CDSXYParticle* p in particles) {
        total += p.x;
    }
    
    return total/(double)particles.count;
}

- (double)standardDeviationX:(NSArray*)particles {
    double mean = [self meanX:particles];
    double totalSqDiff = 0;
    for (CDSXYParticle* p in particles) {
        double delta = p.x - mean;
        totalSqDiff += delta * delta;
    }
    
    return totalSqDiff/(double)particles.count;
}

- (double)meanY:(NSArray*)particles {
    double total = 0;
    for (CDSXYParticle* p in particles) {
        total += p.y;
    }
    
    return total/(double)particles.count;
}

- (double)standardDeviationY:(NSArray*)particles {
    double mean = [self meanY:particles];
    double totalSqDiff = 0;
    for (CDSXYParticle* p in particles) {
        double delta = p.y - mean;
        totalSqDiff += delta * delta;
    }
    
    return totalSqDiff/(double)particles.count;
}
@end
