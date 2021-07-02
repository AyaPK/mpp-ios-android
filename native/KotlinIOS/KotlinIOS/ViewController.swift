import UIKit
import SharedCode

class ViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource, UITableViewDataSource ,ApplicationContractView, UITableViewDelegate {
    func updateDisplayStations(stations: NSMutableArray) {
        
    }
    

    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        stations.count
    }
    
    @IBOutlet private var label: UILabel!
    @IBOutlet private var submitButton: UIButton!
    @IBOutlet private var departurePicker: UIPickerView!
    @IBOutlet private var arrivalPicker: UIPickerView!
    @IBOutlet var simpleTable: UITableView!
    var dataSource: [OutboundJourneys] = []
    var stations = ["Edinburgh Waverley [EDB]", "Newcastle [NCL]", "York [YRK]", "Kings Cross St. Pancras [KGX]", "Leeds [LDS]", "Doncaster [DON]"]
    
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        
        self.departurePicker.delegate = self
        self.departurePicker.dataSource = self
        
        self.arrivalPicker.delegate = self
        self.arrivalPicker.dataSource = self
        
        self.simpleTable.dataSource = self;
        self.simpleTable.delegate = self;
    }

    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
            return stations[row]
        }
    
    @IBAction func submitStations(_ sender : AnyObject){
        let departurePicked = stations[departurePicker.selectedRow(inComponent: 0)].split(separator: " ").last!.replacingOccurrences(of: "[", with: "").replacingOccurrences(of: "]", with: "")
        let arrivalPicked = stations[arrivalPicker.selectedRow(inComponent: 0)].split(separator: " ").last!.replacingOccurrences(of: "[", with: "").replacingOccurrences(of: "]", with: "")
        
        let dateFormat = DateFormatter()
        dateFormat.dateFormat = "yyyy-MM-dd'T'HH'%3A'mm"
        var date = Date()
        date = date.addingTimeInterval(70)
        let formattedDate = dateFormat.string(from: date)
        
        presenter.requestFromAPI(departureCode: departurePicked, arrivalCode: arrivalPicked, currentDateAndTime: formattedDate)
    }
    

    func updateResults(data: [OutboundJourneys]) {
        dataSource = data
        self.simpleTable.reloadData()
    }
    
    func setLabel(text: String) {
        label.text = text
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: "myCell") {
                    // Set text of textLabel.
                    // ... Use indexPath.item to get the current row index.
                    if let label = cell.textLabel {
                        let journey = dataSource[indexPath.item]
                        label.text = journey.originStation.displayName + " > " + journey.destinationStation.displayName + " At " + journey.departureTime.split(separator: "T")[1].split(separator: "+")[0].split(separator: ".")[0]
                    }
                    // Return cell.
                    return cell
                }
                // Return empty cell.
                return UITableViewCell()
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
            // Use length of array a stable row count.
        return dataSource.count
        }
}

