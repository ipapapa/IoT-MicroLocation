//
//  CDSParticleFilter.h
//  CDSParticleFilter
//
//  Created by Andrew Craze on 2/14/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CDSParticleBase.h"


@class CDSParticleFilter;


/**
 The delegate for the CDSParticleFilter must adopt the CDSParticleFilterDelegate protocol.
 This is where the application-specific work of generating particles, applying movement,
 and applying noise to resampled particles is done.
 */
@protocol CDSParticleFilterDelegate <NSObject>

/**
 Requests a new particle from the delegate.  New particles should be appropriately
 distributed in the state space.  (Typically uniformly distributed in the space.)
 
 @param filter The CDSParticleFilter requesting the particle
 @return A new particle which inherits from CDSParticleBase
 */
- (CDSParticleBase*)newParticleForParticleFilter:(CDSParticleFilter*)filter;

/**
 Requests the delegate to apply movement to the particle.
 @param filter The CDSParticleFilter requesting the particle
 @param particle The particle to which movement should be applied
 */
- (void)particleFilter:(CDSParticleFilter*)filter moveParticle:(CDSParticleBase*)particle;

/**
 Requests the delegate to rate the likelihood of the state described in a given particle
 is correct.  Likelihoods will be normalized by the filter, so it's not neccesary to
 ensure that they sum to 1.00.
 @param filter The CDSParticleFilter requesting the likelihood
 @param particle The particle representing the state to be evaluated
 @param measurements Measurements for which the particle's likelihood will be calculated
 @return The (unnormalized) likelihood of the particle's state being "correct"
 */
- (double)particleFilter:(CDSParticleFilter*)filter getLikelihood:(CDSParticleBase*)particle withMeasurements:(id)measurements;

/**
 Requests the delegate to generate a new particle with applied noise from an earlier
 particle.  This peturbation allows the filter to "spread out" across the state space.
 @param filter The CDSParticleFilter requesting the likelihood
 @param particle The particle on which to base the new particle
 @return A new particle based on the original particle, plus applied noise
 */
- (CDSParticleBase*)particleFilter:(CDSParticleFilter*)filter particleWithNoiseFromParticle:(CDSParticleBase*)particle;

@optional
/**
 Optional method which notifies the delegate that all particle weights have been normalized,
 (but not yet resampled.)
 @param filter The CDSParticleFilter
 @param particles The weighted particles
 */
- (void)particleFilter:(CDSParticleFilter*)filter particlesNormalized:(NSArray*)particles;

/**
 Optional method which notifies the delegate that all particles have been resampled.
 (i.e. one generation is complete.)
 @param filter The CDSParticleFilter requesting the likelihood
 @param particles The new generation of particles
 */
- (void)particleFilter:(CDSParticleFilter*)filter particlesResampled:(NSArray*)particles;

@end



/**
 CDSParticleFilter implements a simple particle filter.
 */
@interface CDSParticleFilter : NSObject

/**
 The object that acts as the delegate of the particle filter.
 
 A delegate that is called to initialize particles, apply movement, determine likelihood
 and resample selected particles.
 
 @warning A delegate is required for the particle filter to function.
 */
@property (nonatomic, strong) id<CDSParticleFilterDelegate> delegate;


/**
 The current set of particles
 */
@property (nonatomic, readonly) NSArray* particles;

/**
 Initializes a 'CDSParticleFilter' object with a number of particles specified by
 particleCount.  The particles will be created later (on-demand) by calls to the
 delegate.
 
 @param particleCount The number of particles used by this particle filter
 @return The newly-initialized particle filter.
 */
- (instancetype)initWithParticleCount:(NSUInteger)particleCount;

/**
 Initiate the "move" step of the particle filter.  The filter will call its delegate
 to apply motion to each particle in turn.
 */
- (void)applyMotion;

/**
 Initiate the "resample" step of the particle filter.  The filter will call its delegate
 to evaluate likelihoods, then normalize weights and select particles for resampling.
 @param measurements Measurements to be passed to the likelihood calculation function
 */
- (void)applyMeasurements:(id)measurements;


@end
