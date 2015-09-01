//
//  CDSArenaView.h
//  micro-location
//
//  Created by Fahim Zafari on 2/18/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CDSXYParticle.h"
#import "CDSBeaconLandmark.h"


@interface CDSArenaView : UIView

@property (nonatomic, copy) NSArray* particles;
@property (nonatomic, copy) NSArray* landmarks;
@property (nonatomic, assign) double pxPerMeter;

@end