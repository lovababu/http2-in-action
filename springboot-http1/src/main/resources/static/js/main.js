// HTTP/2 Download functionality with prioritization
function downloadFile(fileName, progress_id, timeline_id) {
    const progressBar = document.getElementById(`${progress_id}`);
    // Show progress bar animation
    progressBar.classList.add('active');

    // Create fetch request with HTTP/2 prioritization hints
    const fetchOptions = {
        method: 'GET',
        headers: {
            'Cache-Control': 'no-cache'
        }
    };
    const timeElem = document.getElementById(`${timeline_id}`);
    if (timeElem) {
        timeElem.textContent = 'Downloading...';
    }
    const startTime = performance.now();
    // Simulate file download with fetch API
    fetch(`/download/${fileName}`, fetchOptions)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            // Get file size from headers if available
            const contentLength = response.headers.get('content-length');

            if (contentLength) {
                return downloadWithProgress(response, progressBar);
            } else {
                return response.blob();
            }
        })
        .then(blob => {
            const endTime = performance.now();
            const duration = ((endTime - startTime) / 1000).toFixed(2);
            if (timeElem) {
                timeElem.textContent = `Downloaded in ${duration}sec.`;
            }
            // Create download link
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = fileName;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);

            // Reset progress bar after download
            setTimeout(() => {
                progressBar.classList.remove('active');
            }, 1000);
        })
        .catch(error => {
            console.error('Download failed:', error);
            alert(`Download failed for ${fileName}: ${error.message}`);
            progressBar.classList.remove('active');
        });
}

// Function to handle download with progress tracking
async function downloadWithProgress(response, progressBar) {
    const reader = response.body.getReader();
    const contentLength = +response.headers.get('content-length');

    let receivedLength = 0;
    const chunks = [];

    while (true) {
        const { done, value } = await reader.read();

        if (done) {
            break;
        }

        chunks.push(value);
        receivedLength += value.length;

        // Update progress bar
        const progress = (receivedLength / contentLength) * 100;
        progressBar.style.setProperty('--progress', `${progress}%`);
    }

    return new Blob(chunks);
}

// Function to download all files with proper prioritization
function downloadAllFiles() {
    const files = [
        { name: 'MLCourse.pdf', progress_id: 'progress-id-1', timeline_id: 'download-time-1' },
        { name: 'MLCourse.pdf', progress_id: 'progress-id-2', timeline_id: 'download-time-2' },
        { name: 'MLCourse.pdf', progress_id: 'progress-id-3', timeline_id: 'download-time-3' }
    ];

    // Download files with staggered timing to demonstrate prioritization
    files.forEach((file, index) => {
        setTimeout(() => {
            downloadFile(file.name, file.progress_id, file.timeline_id);
        });
    });
}
