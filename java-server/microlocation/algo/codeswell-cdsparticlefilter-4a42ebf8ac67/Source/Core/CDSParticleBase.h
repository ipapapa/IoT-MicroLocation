//
//  CDSParticleBase.h
//  CDSParticleFilter
//
//  Created by Andrew Craze on 2/14/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#import <Foundation/Foundation.h>


/**
 This is a base class from which particles for the CDSParticleFilter should inherit.
 */
@interface CDSParticleBase : NSObject 

/**
 particleWeight is used internally by the CDSParticleFilter to track the relative
 likelihood of a particle's state.  These weights are used to select particles for
 resampling.
 */
@property (nonatomic, readonly) double particleWeight;

/**
 accumulatedWeight is used internally by the CDSParticleFilter to track the relative
 likelihood of a particle's state.  These weights are used to select particles for
 resampling.
 */
@property (nonatomic, readonly) double accumulatedWeight;

@end
