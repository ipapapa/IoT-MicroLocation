//
//  SecondViewController.swift
//  FinalIoTMicrolocation
//
//  Created by Ibrahim Darwish on 10/6/15.
//  Copyright © 2015 NCSU. All rights reserved.
//

import UIKit
import Foundation
import CoreLocation

class SecondViewController: UIViewController, CLLocationManagerDelegate, UITableViewDataSource, UITableViewDelegate {
    
    @IBOutlet var tableView: UITableView!
    
    var beacons: [CLBeacon]?
    var lastProximity: CLProximity?
    let locationManager = CLLocationManager()
    let region = CLBeaconRegion(proximityUUID: NSUUID(UUIDString: "F4913F46-75F4-9134-913F-4913F4913F49")!, identifier: "NCSUGimbaliBeacons")
    
    //Identifies the Gimbal UUID Beacon Region
    
    // let region = CLBeaconRegion(proximityUUID: NSUUID(UUIDString: "B9407F30-F5F8-466E-AFF9-25556B57FE6D")!, identifier: "NCSUEstimoteiBeacons")
    
    //Identifies the Estimote UUID Beacon Region
    
    
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
        
        //Sets the tab of the SecondViewController to Black
        
    }
    
    
    func tableView(tableView: UITableView,
        numberOfRowsInSection section: Int) -> Int {
            if(beacons != nil) {
                return beacons!.count
            } else {
                return 0
            }
    
    //This function checks for the number of iBeacons in the room and populates the number of rows in the table based on this.
    
    }
    
    func tableView(tableView: UITableView,
        cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
            var cell:UITableViewCell? =
            tableView.dequeueReusableCellWithIdentifier("MyIdentifier")
            
            if(cell == nil) {
                cell = UITableViewCell(style: UITableViewCellStyle.Subtitle, reuseIdentifier: "MyIdentifier")
                cell!.selectionStyle = UITableViewCellSelectionStyle.None
            }
            
            let beacon:CLBeacon = beacons![indexPath.row]
            var proximityLabel:String! = ""
            
            switch beacon.proximity {
            case CLProximity.Far:
                proximityLabel = "Far"
            case CLProximity.Near:
                proximityLabel = "Near"
            case CLProximity.Immediate:
                proximityLabel = "Immediate"
            case CLProximity.Unknown:
                proximityLabel = "Unknown"
            }
            
            cell!.textLabel!.text = proximityLabel
            
            let detailLabel:String = "Major: \(beacon.major.integerValue), " +
                "Minor: \(beacon.minor.integerValue), " +
                "RSSI: \(beacon.rssi as Int), " + "Proximity: \(proximityLabel), " + "UUID: \(beacon.proximityUUID.UUIDString)"
            cell!.detailTextLabel!.text = detailLabel
            
            
            return cell!
    
    //This function populates the table with actual iBeacon data
    }
    
    
    
    
    func locationManager(manager: CLLocationManager, didRangeBeacons beacons: [CLBeacon], inRegion region: CLBeaconRegion) {
        
        self.tableView.dataSource = self
        self.beacons = beacons
        self.tableView!.reloadData()
        
    //This function brings in the iBeacon data and sends it to be displayed on the table.
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
}
