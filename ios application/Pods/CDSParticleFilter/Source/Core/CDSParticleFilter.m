//
//  CDSParticleFilter.m
//  CDSParticleFilter
//
//  Created by Andrew Craze on 2/14/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#import "CDSParticleFilter.h"

#ifndef LOG_VERBOSE
#define DDLogVerbose(...)
//#define DDLogVerbose(...) NSLog(__VA_ARGS__)
#endif


@interface CDSParticleFilter ()
@property (nonatomic, assign) NSUInteger particleCount;
@property (nonatomic, strong) NSArray* particles;
@end


@interface CDSParticleBase ()
// Expose these otherwise read-only properties
@property (nonatomic, assign) double particleWeight;
@property (nonatomic, assign) double accumulatedWeight;
@end


@implementation CDSParticleFilter

- (instancetype)initWithParticleCount:(NSUInteger)particleCount {
    self = [super init];
    if (self) {
        _particleCount = particleCount;
        DDLogVerbose(@"[%@] Instantiated.", self.class);
    }
    return self;
}


- (void)applyMotion {
    NSAssert(self.delegate != nil, @"%@ requires a delegate", self.class);
    [self ensureParticlesExist];
    
    // Apply motion
    for (id particle in self.particles) {
        [self.delegate particleFilter:self moveParticle:particle];
    }
    
    DDLogVerbose(@"[%@] Motion applied.", self.class);
}


- (void)applyMeasurements:(id)measurements {
    NSAssert(self.delegate != nil, @"%@ requires a delegate", self.class);
    [self ensureParticlesExist];
    
    // Determine likelihoods
    double totalLikelihood = 0.0;
    for (CDSParticleBase* particle in self.particles) {
        particle.particleWeight = [self.delegate particleFilter:self getLikelihood:particle withMeasurements:measurements];
        totalLikelihood += particle.particleWeight;
    }

    // Normalize and accumulate likelihoods
    // Since they are thus in ascending order, no need to sort them
    double accumulatedLikelihood = 0;
    for (CDSParticleBase* particle in self.particles) {
        particle.particleWeight = (particle.particleWeight / totalLikelihood);
        accumulatedLikelihood += particle.particleWeight;
        particle.accumulatedWeight = accumulatedLikelihood;
    }
    
    // Notify delegate we've normalized the weights
    if ([self.delegate respondsToSelector:@selector(particleFilter:particlesNormalized:)]) {
        [self.delegate particleFilter:self particlesNormalized:self.particles];
    }
    
    // Sample particles and regenerate with noise
    NSMutableArray* newParticles = [[NSMutableArray alloc] initWithCapacity:self.particleCount];
    CDSParticleBase* searchParticle = [[CDSParticleBase alloc] init];
    NSRange searchRange = NSMakeRange(0, self.particles.count);
    for (CDSParticleBase* particle in self.particles) {
        searchParticle.accumulatedWeight = (double)drand48();
        NSUInteger i = [self.particles indexOfObject:searchParticle
                                       inSortedRange:searchRange
                                             options:NSBinarySearchingInsertionIndex
                                     usingComparator:^NSComparisonResult(CDSParticleBase* p1, CDSParticleBase* p2) {
                                         if (p1.accumulatedWeight == p2.accumulatedWeight) return NSOrderedSame;
                                         return (p1.accumulatedWeight < p2.accumulatedWeight ? NSOrderedAscending : NSOrderedDescending);
                                     }];
        NSAssert(i < self.particles.count, @"Internal error: selected a particle index beyond the max");
        CDSParticleBase* particle = self.particles[i];
        [newParticles addObject:[self.delegate particleFilter:self particleWithNoiseFromParticle:particle]];
    }
    self.particles = newParticles;

    // Notify delegate we're done
    if ([self.delegate respondsToSelector:@selector(particleFilter:particlesResampled:)]) {
        [self.delegate particleFilter:self particlesResampled:self.particles];
    }
    DDLogVerbose(@"[%@] Particles resampled.", self.class);
}


#pragma mark Helpers

- (void)ensureParticlesExist {
    
    if (self.particles == nil) {
        DDLogVerbose(@"[%@] Initializing %ld particles.", self.class, (long)self.particleCount);
        
        NSMutableArray* tmpParticles = [[NSMutableArray alloc] initWithCapacity:self.particleCount];
        for (NSUInteger i = 0; i < self.particleCount; i++) {
            tmpParticles[i] = [self.delegate newParticleForParticleFilter:self];
        }
        
        self.particles = tmpParticles;
    }
}



@end
