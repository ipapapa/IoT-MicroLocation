//
//  CDSArenaView.h
//  iBeaconLocalization
//
//  Created by Andrew Craze on 12/12/13.
//  Copyright (c) 2013 Codeswell. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CDSXYParticle.h"
#import "CDSBeaconLandmark.h"


@interface CDSArenaView : UIView

@property (nonatomic, copy) NSArray* particles;
@property (nonatomic, copy) NSArray* landmarks;
@property (nonatomic, assign) double pxPerMeter;

@end
