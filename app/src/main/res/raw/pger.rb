require 'pg'
require 'json'

conn = PG.connect( dbname: 'remap', user: 'remap_client', password: 'Remap88' )
conn.exec( "SELECT * FROM remap_nyc.lines" ) do |result|
  puts "result is had"
  result.each do |row|
    puts row.to_s
    #puts row.values_at('color')
  end
end



stops = JSON.parse(File.read('stops_normalized.json'))
transfers = JSON.parse(File.read('transfers_normalized.json'))['transfers']
new_stops = {}

# Add unique stations to db, and bundle stations together


transfers.each do |xf|
    station_deets = stops[xf[0]]

    safe_name = station_deets['name'].gsub("'","''")
    station_insert = "INSERT INTO remap_nyc.stations (id, name, latitude, longitude) values (nextval('remap_nyc.station_id_seq'), '#{safe_name}', #{station_deets['latitude']}, #{station_deets['longitude']})"
    puts station_insert
    conn.exec(station_insert) do |result|
        puts result.to_s
    end

    conn.exec("SELECT currval('remap_nyc.station_id_seq') as id") do |result|
        station_deets['id'] = result[0]['id']
    end

    xf.each do |stop|
         new_stops[stop] = station_deets
    end
end

# Fill Out Line Edges

conn.exec("delete from remap_nyc.line_edges where 1=1") do |result|
    puts result.to_s
end

LINE_IDS = {
"1"=> 0,
"2"=> 2,
"3"=> 3,
"4"=> 4,
"5"=> 5,
"6"=> 6,
"7"=> 7,
"7X"=> 8,
"A"=> 9,
"C"=> 10,
"E"=> 11,
"B"=> 12,
"D"=> 13,
"F"=> 14,
"M"=> 15,
"G"=> 16,
"H"=> 17,
"SI"=> 18,
"J"=> 19,
"Z"=> 20,
"N"=> 21,
"Q"=> 22,
"R"=> 23,
"L"=> 24,
"GS"=> 25,
"FS"=> 26
}

lines = JSON.parse(File.read('lines_normalized.json'))


lines.each do |name, orders|
    ['S','N'].each do |direction|
        order = orders[direction]
        dirbit = (direction == 'S' ? -1 : 1)

        last_station = nil
        ixy = 0
        order.each do |sid|
            station_deets = new_stops[sid]

            if( !station_deets )
                station_deets = stops[sid]
                safe_name = station_deets['name'].gsub("'","''")
                station_insert = "INSERT INTO remap_nyc.stations
                    (id, name, latitude, longitude)
                    values (nextval('remap_nyc.station_id_seq'), '#{safe_name}', #{station_deets['latitude']}, #{station_deets['longitude']})"
                puts station_insert
                conn.exec(station_insert) do |result|
                    puts result.to_s
                end

                conn.exec("SELECT currval('remap_nyc.station_id_seq') as id") do |result|
                    station_deets['id'] = result[0]['id']
                end
            end

            if( !last_station )
                last_station = station_deets
                next
            end

            puts station_deets.inspect
            puts sid

            edge_insert = "INSERT INTO remap_nyc.line_edges
                (id, line_id, from_station, to_station, from_date, to_date, priority, direction, vehicle, trailhead )
                values (
                    nextval('remap_nyc.line_edge_id_seq'),
                    #{LINE_IDS[name]},
                    #{last_station['id']},
                    #{station_deets['id']},
                    '1900-01-01', '3000-01-01',
                    0, #{dirbit}, 0, #{(ixy == 0 ? 'true' : 'false')}
                )"
            puts edge_insert
            conn.exec(edge_insert) do |result|
                puts result.to_s
            end

            ixy = ixy + 1
            last_station = station_deets
        end
    end
end
