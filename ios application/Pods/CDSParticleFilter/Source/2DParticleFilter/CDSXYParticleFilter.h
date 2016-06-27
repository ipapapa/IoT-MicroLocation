//
//  CDSXYParticleFilter.h
//  CDSParticleFilter
//
//  Created by Andrew Craze on 2/14/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//


#include "CDSXYParticle.h"


@class CDSXYParticleFilter;


/**
 The delegate for the CDSXYParticleFilter must adopt the CDSXYParticleFilterDelegate
 protocol.  This is where the application-specific work of applying movement, and
 weighting particles for resampling is done.
 */
@protocol CDSXYParticleFilterDelegate <NSObject>

/**
 Requests the delegate to apply movement to the particle.
 @param filter The CDSParticleFilter requesting the particle
 @param particle The particle to which movement should be applied
 */
- (void)xyParticleFilter:(CDSXYParticleFilter*)filter moveParticle:(CDSXYParticle*)particle;

/**
 Requests the delegate to rate the likelihood of the state described in a given particle
 is correct.  Likelihoods will be normalized by the filter, so it's not neccesary to
 ensure that they sum to 1.00.
 @param filter The CDSParticleFilter requesting the likelihood
 @param particle The particle representing the state to be evaluated
 @param measurements Measurements for which the particle's likelihood will be calculated
 @return The (unnormalized) likelihood of the particle's state being "correct"
 */
- (double)xyParticleFilter:(CDSXYParticleFilter*)filter getLikelihood:(CDSXYParticle*)particle withMeasurements:(id)measurements;

@optional

/**
 Optional method to calculate noise for resampled particles.
 
 If not implemented, the CDSXYParticleFilter will apply gaussian noise with the
 sigma provided when the filter was instantiated.  Delegates can implement this
 method to use a non-gaussian method for applying noise.  This will be called once
 for each dimension (X and Y) for each particle being resampled.
 @param filter The CDSParticleFilter requesting the noise
 @param m The mean for the noise to be created
 @param s The sigma for the noise to be created
 @return The new value to be used for that dimension
 */
- (double)xyParticleFilter:(CDSXYParticleFilter*)filter noiseWithMean:(double)m sigma:(double)s;

/**
 Optional method which notifies the delegate that all particle weights have been normalized,
 (but not yet resampled.)
 @param filter The CDSParticleFilter
 @param particles The weighted particles
 */
- (void)xyParticleFilter:(CDSXYParticleFilter*)filter particlesNormalized:(NSArray*)particles;

/**
 Optional method which notifies the delegate that all particles have been resampled.
 (i.e. one generation is complete.)
 @param filter The CDSParticleFilter
 @param particles The new generation of particles
 */
- (void)xyParticleFilter:(CDSXYParticleFilter*)filter particlesResampled:(NSArray*)particles;

@end


/**
 CDSXYParticleFilter implements a simple 2-dimensional (X-Y) particle filter.
 */
@interface CDSXYParticleFilter : NSObject

/**
 The object that acts as the delegate of the particle filter.
 
 A delegate that is called to apply movement, and determine likelihood for resampling
 selected particles.
 
 @warning A delegate is required for the particle filter to function.
 */
@property (nonatomic, strong) id<CDSXYParticleFilterDelegate> delegate;

/**
 Initializes a 'CDSParticleFilter' object with a number of particles specified by
 particleCount.
 
 The particles will be created with a state space of minX <= x <= maxX and
 minY <= y <= maxY.  The initial particles will be randomly/uniformly distributed
 across the state space and the filter will apply gaussian noise with σ = noiseSigma
 each time the particles are resampled.
 
 @param particleCount The number of particles used by this particle filter
 @param minX The lower limit for X in the state space
 @param maxX The upper limit for X in the state space
 @param minY The lower limit for Y in the state space
 @param maxY The upper limit for Y in the state space
 @param sigma The standard deviation for noise applied when resampling particles
 @return The newly-initialized particle filter.
 */
- (instancetype)initWithParticleCount:(NSUInteger)particleCount
                       minX:(double)minX
                       maxX:(double)maxX
                       minY:(double)minY
                       maxY:(double)maxY
                 noiseSigma:(double)sigma;

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
