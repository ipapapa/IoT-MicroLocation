//
//  CDSParticleFilterTests.m
//  CDSParticleFilterTests
//
//  Created by Andrew Craze on 2/14/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "CDSParticleFilter.h"

@interface CDSParticleFilterTests : XCTestCase <CDSParticleFilterDelegate>

@property (nonatomic, assign) NSUInteger moveCount;
@property (nonatomic, assign) NSUInteger likelihoodCount;
@property (nonatomic, assign) NSUInteger resampleCount;
@property (nonatomic, assign) NSUInteger returnedCount;

@end

@implementation CDSParticleFilterTests

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

static NSUInteger particleCount = 100;

- (void)testParticleCount {
    self.moveCount = 0;
    self.likelihoodCount = 0;
    self.resampleCount = 0;
    self.returnedCount = 0;
    
    CDSParticleFilter* filter = [[CDSParticleFilter alloc] initWithParticleCount:particleCount];
    filter.delegate = self;
    
    [filter applyMotion];
    XCTAssertEqual(particleCount, self.moveCount, @"applyMotion did not call back for all particles");
    
    [filter applyMeasurements:nil];
    XCTAssertEqual(particleCount, self.likelihoodCount, @"applyMeasurements did not call back for all particles' likelihoods");
    XCTAssertEqual(particleCount, self.resampleCount, @"applyMeasurements did not call back to resample all particles");
    
    XCTAssertEqual(particleCount, self.returnedCount, @"incorrect number of particles returned to resampling notification");
}


#pragma mark - CDSParticleFilterDelegate protocol

- (CDSParticleBase*)newParticleForParticleFilter:(CDSParticleFilter*)filter {
    return [[CDSParticleBase alloc] init];
}

- (void)particleFilter:(CDSParticleFilter*)filter moveParticle:(CDSParticleBase*)particle {
    self.moveCount++;
    return;
}

- (double)particleFilter:(CDSParticleFilter*)filter getLikelihood:(CDSParticleBase*)particle withMeasurements:(id)measurements {
    self.likelihoodCount++;
    return drand48();
}

- (CDSParticleBase*)particleFilter:(CDSParticleFilter*)filter particleWithNoiseFromParticle:(CDSParticleBase*)particle {
    self.resampleCount++;
    return [[CDSParticleBase alloc] init];
}

- (void)particleFilter:(CDSParticleFilter *)filter particlesResampled:(NSArray *)particles {
    self.returnedCount = particles.count;
}



@end
