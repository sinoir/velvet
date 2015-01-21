require 'csv'
require 'json'

stops = {}
CSV.foreach("stops.txt") do |row|
  next if row[0].length != 3
  
  stop_id = row[0]
  
  unless stops.has_key? stop_id
	stops[stop_id] = {}
  end
  stops[stop_id] = {
    name: row[2],
    latitude: row[4].to_f,
    longitude: row[5].to_f
  }
end



lines = {}
CSV.foreach("stop_times.txt") do |row|
  next if row[0] == 'trip_id'
  
  line_label = row[0][20..22].delete(".")
  direction = row[0][23]
  line_id = line_label + direction
  
  stop_id = row[3][0..2]
  stop_seq = row[4].to_i - 1 # For Starting at 0
  
  unless lines.has_key? line_label
	lines[line_label] = {}
  end
  line = lines[line_label]
  
  unless line.has_key? direction
    line[direction] = []
  end
  dirline = line[direction]
  
  unless dirline[stop_seq]
    dirline[stop_seq] = stop_id
  end
end


transfers = []
CSV.foreach("transfers.txt") do |row|
  next if row[0] == 'from_stop_id'
  from_id = row[0]
  to_id = row[1]
  
  found = false
  transfers.each do |transfer|
	if transfer.include?(from_id) && !transfer.include?(to_id)
		transfer << to_id
		found = true
	elsif transfer.include?(to_id) && !transfer.include?(from_id)
		transfer << from_id
		found = true
	elsif transfer.include?(from_id) && from_id == to_id
		found = true
	end
  end
  unless found
    if from_id == to_id
		transfers << [from_id]
	else
		transfers << [from_id, to_id]
	end
  end
end



File.open('stops_normalized.json', 'w'){ |file| file.write stops.to_json }
File.open('lines_normalized.json', 'w'){ |file| file.write lines.to_json }
File.open('transfers_normalized.json', 'w'){ |file| file.write({transfers: transfers}.to_json) }
