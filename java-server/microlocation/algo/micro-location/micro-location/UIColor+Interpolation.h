//
//  UIColor+Interpolation.h
//  micro-location
//
//  Created by Fahim Zafari on 2/18/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//
#import <UIKit/UIKit.h>

@interface UIColor (Interpolation)

- (UIColor *)colorByInterpolatingWith:(UIColor *)color factor:(CGFloat)factor;

@end
