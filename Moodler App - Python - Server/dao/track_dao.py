from datetime import date, datetime, timedelta
from utils.python_database_connector import DatabaseConnector
from injector import inject as Inject
from dtos.track_dto import Track

class TrackDao:

    def __init__(self):
        self.db = DatabaseConnector()

    def __new__(cls):
       if not hasattr(cls, 'instance'):
           cls.instance = super(TrackDao, cls).__new__(cls)
       return cls.instance
    
    def get_all_track(self):
        query = "SELECT * FROM track"
        self.db.executeQuery(query)
        return list(map(lambda track: self.map_track(track),self.db.getQueryResult()))
    
    def get_track_by_id(self,id):
        query = "SELECT * FROM track WHERE track.id='%s'" % id
        self.db.executeQuery(query)
        return list(map(lambda track: self.map_track(track),self.db.getQueryResult()))
    
    def insert_track(self,json_params):
        params = (json_params['id'],json_params['track_id'],json_params['playlist_id'])
        query = "INSERT INTO track (id,track_id,playlist_id) VALUES ('%s','%s','%s')" % params
        return self.db.executeQuery(query,isInsert=True)

    def delete_track(self,id):
        query = "DELETE FROM track WHERE track.id='%s'" % id
        return self.db.executeQuery(query,isInsert=True)
    
    def map_track(self,track_input):   
        track = Track(
            track_input['id'],
            track_input['track_id'],
            track_input['playlist_id'],
        )
        return track.__dict__