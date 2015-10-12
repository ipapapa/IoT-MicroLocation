//
//  FirstViewController.swift
//  FinalIoTMicrolocation
//
//  Created by Ibrahim Darwish on 10/6/15.
//  Copyright © 2015 NCSU. All rights reserved.
//

import UIKit
import Alamofire
import Foundation
import CoreLocation



class FirstViewController: UIViewController, CLLocationManagerDelegate {
    
    @IBOutlet var SeniorDesignLabMap: UIImageView!
    
    let locationManager = CLLocationManager()
    let region = CLBeaconRegion(proximityUUID: NSUUID(UUIDString: "B9407F30-F5F8-466E-AFF9-25556B57FE6D")!, identifier: "NCSUEstimoteiBeacons")
    
      //Identifies the Estimote UUID Beacon Region
    
    var BlueDotExists = false
    var BlueDot:UIImageView!

    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
   
        locationManager.delegate = self
        if (CLLocationManager.authorizationStatus() != CLAuthorizationStatus.AuthorizedWhenInUse) {
            locationManager.requestWhenInUseAuthorization()
        }
        
        //Prompts user to allow them to be located
        
        locationManager.startRangingBeaconsInRegion(region)
        
        //Starts ranging Estimote iBeacons
        
        self.tabBarController!.tabBar.barTintColor = UIColor .blackColor()
    
        //Sets the tab of the FirstViewController to Black
        
    }
    
    func locationManager(manager: CLLocationManager,
        didRangeBeacons beacons: [CLBeacon],
        inRegion region: CLBeaconRegion) {
    
            //Beacons are ranged here and assuming that we have beacons we can jump into this for loop!
            
            for beacon in beacons {
                
                let minor = beacon.minor .stringValue
                let rssi = beacon.rssi.description
                let beaconparameters: [String: AnyObject]! = ["minor": minor, "rssi": rssi]
                
                //Extracting beacon minor values to identify beacons individually and also rssi and placing them as parameters to POST to the Server for Microlocation Calculations!
                
                Alamofire.request(.POST, "http://10.139.67.124:8080/microlocation/WemoController3", parameters: beaconparameters)
                    .responseJSON { response in
                        
                        //Once we make the post above to whatever URL we are interested in we come down here and here is the response from the server where the JSON Object is parsed
                        
                        if let JSON = response.result.value {
                            
                            let xcoordinate = JSON.valueForKey("xcoordinate")!
                            let ycoordinate = JSON.valueForKey("ycoordinate")!
                            
                            //Here I extract what I need out of the server response which are the X and Y Coordinates of my location
                            
                            let xFloat = xcoordinate.doubleValue!
                            let yFloat = ycoordinate.doubleValue!
                           
                            //I have to make a couple of conversions to get Swift to accept my numbers for plotting
                            
                            let xFloatFinal =   CGFloat(xFloat)
                            let yFloatFinal = CGFloat(yFloat)
                            
                            let xConversionFactor = CGFloat(280/6.12139)
                            let yConversionFactor = CGFloat(400/9.3472)
                            
                            //Here I specified a conversion factor for each coordinate point to convert meters to points on an (x,y) coordinate system.
                            
                            let xPlot = ((xConversionFactor)*(xFloatFinal))+10
                            let yPlot = ((yConversionFactor)*(yFloatFinal))+20
                            
                            //These are my final results at which the iPhone will plot onto a map of a room
                            
                            print("xConversionFactor",xConversionFactor)
                            print("yConversionFactor", yConversionFactor)
                            print("xFloat",xFloatFinal)
                            print("yFloat",yFloatFinal)
                            print("xPlot",xPlot)
                            print("yPlot",yPlot)
                            
                            if self.BlueDotExists {
                                self.BlueDot.removeFromSuperview()
                            }
                           
                            //Below is the method that actually plots and above is the if Statement that checks whether or not the User's Location has been plotted and if it has then the old plot of the User's location is removed and a new one is generated in the new location of the user.
                            
                            self.BlueDot  = UIImageView(frame:CGRectMake(xPlot, yPlot, 15, 15));
                            self.BlueDot.image = UIImage(named:"TrackingDot.png")
                            self.view.addSubview(self.BlueDot)
                            
                            self.BlueDotExists = true
                    
                        }
                }
            }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

