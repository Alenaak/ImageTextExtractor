# Image Text Extraction App

This Android app allows users to easily extract text from images using Optical Character Recognition (OCR) powered by Google ML Kit. The app provides a simple interface to select an image, extract any text from it, and interact with the extracted text. Users can copy the text to the clipboard or clear the app's screen to start fresh.

---

## Features
1. **Image Selection**: Users can choose an image from their device using the `ActivityResultContracts.GetContent()` intent, which eliminates the need for camera permissions.
2. **Text Extraction**: The app processes the selected image using Google ML Kit’s Text Recognition API, extracting any recognizable text and displaying it on the screen.
3. **Copy Text to Clipboard**: Once the text is extracted, users can copy it to the clipboard, making it easy to paste into other apps or documents.
4. **Clear Screen**: A button allows users to clear both the selected image and the extracted text, resetting the app for the next task.
5. **User-friendly UI**: The app uses Jetpack Compose to create a clean, intuitive interface with a dark theme. Buttons are clearly laid out for easy access, and the layout is responsive, ensuring usability across different screen sizes.

---

## Technical Implementation

### OCR with Google ML Kit
- The app uses Google ML Kit’s Text Recognition API for efficient and accurate text extraction. This operation runs asynchronously to keep the UI responsive during processing.

### Image Handling
- After the user selects an image, it is converted into a `Bitmap` for OCR processing. The app supports a variety of image formats that are compatible with the OCR engine.

### Clipboard Management
- The extracted text is stored in the clipboard using the `ClipboardManager` API, allowing users to easily copy and paste the text wherever needed.

### State Management
- The app uses Jetpack Compose's `remember` and `mutableStateOf` to manage and update the UI state. This ensures that changes like text extraction or image selection are reflected in the UI without manual refreshes.

---

## User Experience
1. The app follows a simple flow: **Image Selection → Text Extraction → Copy or Clear Text**.
2. The clean, dark-themed UI is designed for both aesthetics and clarity, with high contrast between buttons and text for easy readability.

---

## Error Handling
- **Text Not Found**: If no text is detected in the selected image, a Toast message is displayed to notify the user.
- **OCR Failure**: In the event of an OCR failure, an error message is shown, allowing users to identify issues or troubleshoot.

---

## Conclusion
This app is a straightforward and efficient solution for extracting and interacting with text from images. Using Google ML Kit for OCR and Jetpack Compose for UI design, the app ensures a smooth user experience. It is perfect for tasks like scanning documents, extracting contact information from photos, or quickly capturing text from images for further use.
