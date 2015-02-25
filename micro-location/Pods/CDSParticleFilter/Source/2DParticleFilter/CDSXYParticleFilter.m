//
//  CDSXYParticleFilter.m
//  CDSParticleFilter
//
//  Created by Andrew Craze on 2/14/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#import "CDSXYParticleFilter.h"
#import "CDSParticleFilter.h"
#import "boxmuller.h"

@interface CDSXYParticleFilter () <CDSParticleFilterDelegate>

@property (nonatomic, strong) CDSParticleFilter* filter;
@property (nonatomic, assign) double minX;
@property (nonatomic, assign) double maxX;
@property (nonatomic, assign) double minY;
@property (nonatomic, assign) double maxY;
@property (nonatomic, assign) double noiseSigma;

@end

@implementation CDSXYParticleFilter

- (instancetype)initWithParticleCount:(NSUInteger)particleCount
                       minX:(double)minX
                       maxX:(double)maxX
                       minY:(double)minY
                       maxY:(double)maxY
                 noiseSigma:(double)sigma
{
    self = [super init];
    if (self) {
        _minX = minX;
        _maxX = maxX;
        _minY = minY;
        _maxY = maxY;
        _noiseSigma = sigma;
        _filter = [[CDSParticleFilter alloc] initWithParticleCount:particleCount];
        _filter.delegate = self;
    }
    return self;
}


- (void)applyMotion {
    [self.filter applyMotion];
}


- (void)applyMeasurements:(id)measurements {
    [self.filter applyMeasurements:measurements];
}


#pragma mark - CDSParticleFilterDelegate protocol

- (CDSParticleBase*)newParticleForParticleFilter:(CDSParticleFilter*)filter {
    CDSXYParticle* particle = [[CDSXYParticle alloc] init];
    particle.x = ((double)arc4random() / (double)UINT32_MAX) * (self.maxX - self.minX) + self.minX;
    particle.y = ((double)arc4random() / (double)UINT32_MAX) * (self.maxY - self.minY) + self.minY;
    return particle;
}


- (void)particleFilter:(CDSParticleFilter*)filter moveParticle:(CDSParticleBase*)particle {
    NSAssert(self.delegate != nil, @"%@ requires a delegate", self.class);
    [self.delegate xyParticleFilter:self moveParticle:(CDSXYParticle*)particle];
}


- (double)particleFilter:(CDSParticleFilter*)filter getLikelihood:(CDSParticleBase*)particle withMeasurements:(id)measurements {
    NSAssert(self.delegate != nil, @"%@ requires a delegate", self.class);
    return [self.delegate xyParticleFilter:self getLikelihood:(CDSXYParticle*)particle withMeasurements:measurements];
}


- (CDSParticleBase*)particleFilter:(CDSParticleFilter*)filter particleWithNoiseFromParticle:(CDSParticleBase*)particle {
    CDSXYParticle* p = (CDSXYParticle*)particle;
    
    // Create new particle from old and add noise
    CDSXYParticle* newParticle = [[CDSXYParticle alloc] init];
    
    if ([self.delegate respondsToSelector:@selector(xyParticleFilter:noiseWithMean:sigma:)]) {
        // Delegate-supplied noise
        newParticle.x = [self.delegate xyParticleFilter:self noiseWithMean:p.x sigma:self.noiseSigma];
        newParticle.y = [self.delegate xyParticleFilter:self noiseWithMean:p.y sigma:self.noiseSigma];
    } else {
        // Otherwise, use gaussian noise
        newParticle.x = box_muller(p.x, self.noiseSigma);
        newParticle.y = box_muller(p.y, self.noiseSigma);
    }

    return newParticle;
}


- (void)particleFilter:(CDSParticleFilter*)filter particlesNormalized:(NSArray*)particles {
    if ([self.delegate respondsToSelector:@selector(xyParticleFilter:particlesNormalized:)]) {
        [self.delegate xyParticleFilter:self particlesNormalized:particles];
    }
}


- (void)particleFilter:(CDSParticleFilter*)filter particlesResampled:(NSArray*)particles {
    if ([self.delegate respondsToSelector:@selector(xyParticleFilter:particlesResampled:)]) {
        [self.delegate xyParticleFilter:self particlesResampled:particles];
    }
}


@end
