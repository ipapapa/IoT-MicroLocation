//
//  UIColor+Interpolation.m
//  iBeaconLocalization
//
//  Created by Andrew Craze on 3/1/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#import "UIColor+Interpolation.h"

@implementation UIColor (Interpolation)

// Cribbed from http://stackoverflow.com/a/20327117/46731
- (UIColor *)colorByInterpolatingWith:(UIColor *)color factor:(CGFloat)factor {
    factor = MIN(MAX(factor, 0.0), 1.0);
    
    const CGFloat *startComponent = CGColorGetComponents(self.CGColor);
    const CGFloat *endComponent = CGColorGetComponents(color.CGColor);
    
    float startAlpha = CGColorGetAlpha(self.CGColor);
    float endAlpha = CGColorGetAlpha(color.CGColor);
    
    float r = startComponent[0] + (endComponent[0] - startComponent[0]) * factor;
    float g = startComponent[1] + (endComponent[1] - startComponent[1]) * factor;
    float b = startComponent[2] + (endComponent[2] - startComponent[2]) * factor;
    float a = startAlpha + (endAlpha - startAlpha) * factor;
    
    return [UIColor colorWithRed:r green:g blue:b alpha:a];
}

@end
