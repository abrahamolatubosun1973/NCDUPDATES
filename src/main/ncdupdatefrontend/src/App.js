import React, { useState } from "react";
import axios from "axios";

function App() {
  const [file, setFile] = useState([]);
  const [isUploading, setIsUploading] = useState(false);
  const [uploadResult, setUploadResult] = useState([]);

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    setFile(selectedFile);
  };

  const handleUpload = async (e) => {
    e.preventDefault();

    setIsUploading(true);

    let url = "http://localhost:8012/api/upload";
    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await axios.post(url, formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      const { totalRowsFromCsv, totalRowsFromDatabase, totalRecordsUpdated } =
        response.data;
      console.log(
        "From Csv: " + totalRowsFromCsv,
        "From DB: " + totalRowsFromDatabase,
        "# of DB Update: " + totalRecordsUpdated
      );
      setUploadResult({
        totalRowsFromCsv,
        totalRowsFromDatabase,
        totalRecordsUpdated,
      });

      //alert("File Uploaded Successfully!");
    } catch (error) {
      console.error("Error uploading file: ", error);
      alert("Error uploading file ", error);
    } finally {
      setIsUploading(false);
    }
  };

  return (
    <main>
      <div className='flex items-center justify-center h-screen bg-sky-800'>
        <div className='bg-white p-8 rounded-lg shadow-md '>
          <label className='block text-base text-gray-500 font-semibold mb-4 px-44'>
            Upload excel file
          </label>

          <div className='font-[sans-serif] max-w-md mx-auto'>
            <label className='text-base text-gray-500 font-semibold mb-2 block'></label>
            <input
              name='upfiles'
              label='Upload NYSC'
              type='file'
              accept='.csv,.xlsx,.xls'
              multiple
              onChange={handleFileChange}
              className='w-full text-gray-400 font-semibold text-sm bg-white border file:cursor-pointer cursor-pointer file:border-0 file:py-3 file:px-4 file:mr-4 file:bg-gray-100 file:hover:bg-gray-200 file:text-gray-500 rounded'
            />
            <p className='text-xs text-gray-400 mt-2 mb-8'>
              Only csv files are Allowed.
            </p>
          </div>
          {isUploading ? (
            <div className='font-[sans-serif] space-x-4 space-y-4 text-center'>
              <button
                type='button'
                className='px-6 py-2.5 rounded text-white text-sm tracking-wider font-semibold border-none outline-none bg-blue-600 hover:bg-blue-700 active:bg-blue-600'>
                Processing Uploads
                <svg
                  xmlns='http://www.w3.org/2000/svg'
                  width='18px'
                  fill='#fff'
                  className='ml-2 inline animate-spin'
                  viewBox='0 0 24 24'>
                  <path
                    d='M12 22c5.421 0 10-4.579 10-10h-2c0 4.337-3.663 8-8 8s-8-3.663-8-8c0-4.336 3.663-8 8-8V2C6.579 2 2 6.58 2 12c0 5.421 4.579 10 10 10z'
                    data-original='#000000'
                  />
                </svg>
              </button>
            </div>
          ) : (
            <>
              {uploadResult && (
                <div className='justify-center items-center py-2 pb-8'>
                  <div className='rounded-lg shadow-md px-6'>
                    <p>
                      Total rows read from CSV file:{"  "}
                      {uploadResult.totalRowsFromCsv}
                    </p>
                    <p>
                      Total rows read from database table:{"  "}
                      {uploadResult.totalRowsFromDatabase}
                    </p>
                    <p className='pb-4'>
                      Total number of records updated:{"  "}
                      {uploadResult.totalRecordsUpdated}
                    </p>
                  </div>
                </div>
              )}
              <div className='flex justify-center items-center'>
                <button
                  onClick={handleUpload}
                  className='bg-transparent hover:bg-blue-500 text-blue-700 font-semibold hover:text-white py-2 px-4 border border-blue-500 hover:border-transparent rounded'>
                  Upload
                </button>
              </div>
            </>
          )}
        </div>
      </div>
    </main>
  );
}

export default App;
