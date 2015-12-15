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
    
    
 
    
    let locationManager = CLLocationManager()
    let region = CLBeaconRegion(proximityUUID: NSUUID(UUIDString: "F4913F46-75F4-9134-913F-4913F4913F49")!, identifier: "NCSUGimbaliBeacons")
    
     //Identifies the Gimbal UUID Beacon Region
    
    //let region = CLBeaconRegion(proximityUUID: NSUUID(UUIDString: "B9407F30-F5F8-466E-AFF9-25556B57FE6D")!, identifier: "NCSUEstimoteiBeacons")
    
      //Identifies the Estimote UUID Beacon Region
    
    var xPlot = CGFloat(0)
    var yPlot = CGFloat(0)
  
    var xPlotBlueDot = CGFloat(0)
    var yPlotBlueDot = CGFloat(0)
    
    var BlackSquareExists = false
    var createBlackSquare = false
    var BlackSquare:UIImageView!
    
    var BlueDotExists = false
    var createBlueDot = false
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
        
        //Starts ranging iBeacons
        
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
                
                createBlueDot = false
                createBlackSquare = false
                //Extracting beacon minor values to identify beacons individually and also rssi and placing them as parameters to POST to the Server for Microlocation Calculations!
            
              
                //  Alamofire.request(.POST, "http://10.139.193.13:8080/microlocationServer/interactiveRoom", parameters: beaconparameters)
                
                // This is an example of the local microlocation server to send the iBeacon data
                
                Alamofire.request(.POST, "http://microlocationserver.mybluemix.net/interactiveRoom", parameters: beaconparameters)
                    
                    
                 //This is an example of the IBM Bluemix server to send the iBeacon data

              
                    .responseJSON { response in
                        
                        //Once we make the post above to whatever URL we are interested in we come down here and here is the response from the server where the JSON Object is parsed
                        
                         print (beaconparameters)
                         print(response.result.value)
                        
                         if let JSON = response.result.value {
                            
                            let xcoordinate = JSON.valueForKey("xcoordinate")!
                            let ycoordinate = JSON.valueForKey("ycoordinate")!
                            
                            //Here I extract what I need out of the server response which are the X and Y Coordinates of my location
                            
                            let xFloat = xcoordinate.doubleValue!
                            let yFloat = ycoordinate.doubleValue!
                           
                            //I have to make a couple of conversions to get Swift to accept my numbers for plotting
                            
                            let xFloatFinal = CGFloat(xFloat)
                            let yFloatFinal = CGFloat(yFloat)
            
                            if self.BlackSquareExists {
                                self.BlackSquare.removeFromSuperview()
                            }
                            
                            if self.BlueDotExists {
                                self.BlueDot.removeFromSuperview()
                            }
                            
                            
                            if xFloatFinal < 1 && xFloatFinal > 0 && yFloatFinal < 1 && yFloatFinal > 0 {
                                
                                self.xPlot = CGFloat(7)
                                self.yPlot = CGFloat(221)
                                self.createBlackSquare = true
                                
                                
                            }
                            
                           
                            if xFloatFinal < 2 && xFloatFinal > 1 && yFloatFinal < 1 && yFloatFinal > 0 {
                                
                                self.xPlot = CGFloat(108)
                                self.yPlot = CGFloat(221)
                                self.createBlackSquare = true
                                
                       
                            
                            }
                            
                            if xFloatFinal < 3 && xFloatFinal > 2 && yFloatFinal < 1 && yFloatFinal > 0 {
                                
                                self.xPlot = CGFloat(209)
                                self.yPlot = CGFloat(221)
                                self.createBlackSquare = true
                                
                               
                            
                            }
                            
                            if xFloatFinal < 1 && xFloatFinal > 0 && yFloatFinal < 2 && yFloatFinal > 1 {
                                
                                self.xPlot = CGFloat(7)
                                self.yPlot = CGFloat(312)
                                self.createBlackSquare = true
                                
                                
                            }
                            
                            if xFloatFinal < 2 && xFloatFinal > 1 && yFloatFinal < 2 && yFloatFinal > 1 {
                                
                                self.xPlot = CGFloat(108)
                                self.yPlot = CGFloat(312)
                                self.createBlackSquare = true
                                
                           
                            }
                            
                            if xFloatFinal < 3 && xFloatFinal > 2 && yFloatFinal < 2 && yFloatFinal > 1 {
                                
                                self.xPlot = CGFloat(209)
                                self.yPlot = CGFloat(312)
                                self.createBlackSquare = true
                            
                            }
                            
                      
                            
                            //The above if statements plots the user according to microlocation coordinates received from the server
                            
                            
                            
                            if xFloatFinal == 20 && yFloatFinal == 20 {
                                
                                self.xPlotBlueDot = CGFloat(70)
                                self.yPlotBlueDot = CGFloat(100)
                                self.createBlueDot = true
                              
                                // This plots the proximity location of a user in the upper left part of the screen where the interactive living room is located.
                                
                            }
                            
                            if xFloatFinal == 40 && yFloatFinal == 40 {
                                
                                self.xPlotBlueDot = CGFloat(236)
                                self.yPlotBlueDot = CGFloat(87)
                                self.createBlueDot = true
                                
                                // This plots the proximity location of a user in the upper right part of the screen where the smart desk is located
                                
                            }
                          
                             //Below is the method that actually plots and the if Statement that checks whether or not the User's Location has been plotted and if it has then the old plot of the User's location is removed and a new one is generated in the new location of the user.
                            
                            if(self.createBlackSquare){
                                
                            self.BlackSquare  = UIImageView(frame:CGRectMake(self.xPlot, self.yPlot, 102, 91));
                            self.BlackSquare.image = UIImage(named:"second-1.png")
                            self.view.addSubview(self.BlackSquare)
                            self.BlackSquareExists = true
                            
                            }
      
                            
                            //Below is the method that actually plots and the if Statement that checks whether or not the User's Location has been plotted and if it has then the old plot of the User's location is removed and a new one is generated in the new location of the user.
                            
                            
                            if(self.createBlueDot){
                            self.BlueDot  = UIImageView(frame:CGRectMake(self.xPlotBlueDot, self.yPlotBlueDot, 15, 15));
                            self.BlueDot.image = UIImage(named:"TrackingDot.png")
                            self.view.addSubview(self.BlueDot)
                            self.BlueDotExists = true
                            
                            }
                            

                            
                        }
                }
            }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

