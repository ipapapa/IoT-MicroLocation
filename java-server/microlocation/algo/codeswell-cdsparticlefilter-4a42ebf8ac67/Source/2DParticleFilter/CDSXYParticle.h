//
//  CDSXYParticle.h
//  CDSParticleFilter
//
//  Created by Andrew Craze on 2/14/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#import "CDSParticleBase.h"

/**
 This is the base class from which particles for the CDSXYParticleFilter should
 inherit. It represents a 2-dimensional (X-Y) state space.
 */
@interface CDSXYParticle : CDSParticleBase

/**
 The particle's x value in the state space
 */
@property (nonatomic, assign) double x;

/**
 The particle's y value in the state space
 */
@property (nonatomic, assign) double y;

@end
