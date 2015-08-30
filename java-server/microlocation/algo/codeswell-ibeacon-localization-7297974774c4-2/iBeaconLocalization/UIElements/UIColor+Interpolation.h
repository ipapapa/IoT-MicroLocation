//
//  UIColor+Interpolation.h
//  iBeaconLocalization
//
//  Created by Andrew Craze on 3/1/14.
//  Copyright (c) 2014 Codeswell. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIColor (Interpolation)

- (UIColor *)colorByInterpolatingWith:(UIColor *)color factor:(CGFloat)factor;

@end
