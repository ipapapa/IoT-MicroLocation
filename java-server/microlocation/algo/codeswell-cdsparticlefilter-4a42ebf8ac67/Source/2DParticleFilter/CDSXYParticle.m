//
//  CDSXYParticle.m
//  CDSParticleFilter
//
//  Created by Andrew Craze on 2/14/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#import "CDSXYParticle.h"

@implementation CDSXYParticle

- (NSString*)debugDescription
{
    return [NSString stringWithFormat:@"CDSXYParticle at (%f,%f)", self.x, self.y];
}

@end
