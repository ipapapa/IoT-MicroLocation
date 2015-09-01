//
//  Singleton.m
//  micro-location
//
//  Created by Ian Hamilton on 2/9/15.
//  Copyright (c) 2015 Lost Eon Entertainment. All rights reserved.
//

#import "Singleton.h"

@implementation Singleton

+ (Singleton *)instance {
    static Singleton *_instance = nil;
    
    @synchronized (self) {
        if (_instance == nil) {
            _instance = [[self alloc] init];
            
        }
    }
    
    
    return _instance;
    
}

@end
